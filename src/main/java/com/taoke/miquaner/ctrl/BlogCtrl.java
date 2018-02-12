package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EFeedback;
import com.taoke.miquaner.data.EHelpDoc;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IBlogServ;
import com.taoke.miquaner.serv.exp.SearchTypeException;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.util.StringUtils;
import com.taoke.miquaner.view.FeedbackSubmit;
import com.taoke.miquaner.view.HelpDocSubmit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class BlogCtrl {

    private final static Logger logger = LogManager.getLogger(BlogCtrl.class);

    private final String BlogAdmin;
    private final IBlogServ blogServ;

    @Autowired
    public BlogCtrl(IBlogServ blogServ, Environment env) {
        this.blogServ = blogServ;

        this.BlogAdmin = env.getProperty("taoke.blog.admin");
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/blog/helpdoc/post", method = RequestMethod.POST)
    public Object postHelpdoc(@RequestBody HelpDocSubmit helpDocSubmit) {
        try {
            String filePath = this.blogServ.storeBlog(BlogAdmin, helpDocSubmit.getFileName(), helpDocSubmit.getContent());
            helpDocSubmit.setPath(filePath);
            EHelpDoc eHelpDoc = new EHelpDoc();
            BeanUtils.copyProperties(helpDocSubmit, eHelpDoc);
            EHelpDoc saved = this.blogServ.saveHelpDoc(eHelpDoc);
            return Result.success(saved);
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/blog/helpdoc/del/{id}", method = RequestMethod.GET)
    public Object delHelpdoc(@PathVariable(name = "id") Long id) {
        try {
            this.blogServ.removeHelpDoc(id);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @RequestMapping(value = "/blog/helpdoc/list", method = RequestMethod.GET)
    public Object listHelpdoc() {
        try {
            return Result.success(this.blogServ.listAllHelpDoc());
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @RequestMapping(value = "/blog/helpdoc/{type}/list", method = RequestMethod.GET)
    public Object listHelpdocByType(@PathVariable(name = "type") Integer type) {
        try {
            return Result.success(this.blogServ.listHelpDocByType(type));
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/blog/raw/{path}", method = RequestMethod.GET)
    public Object rowBlogContent(@PathVariable(name = "path") String path) {
        try {
            String content = this.blogServ.fetchBlog(path.replaceAll("&@&", "/"), "");
            return Result.success(this.blogServ.dryMarkdown(content));
        } catch (IOException e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @Auth
    @RequestMapping(value = "/blog/feedback/post", method = RequestMethod.POST)
    public Object postFeedback(@RequestBody FeedbackSubmit feedbackSubmit, HttpServletRequest request) {
        EUser user = (EUser) request.getAttribute("user");
        if (null == user) {
            return Result.unAuth();
        }

        try {
            String filePath = this.blogServ.storeBlog(user.getPhone(), StringUtils.randomMd5(), feedbackSubmit.getContent());
            feedbackSubmit.setPath(filePath);
            EFeedback eFeedback = new EFeedback();
            BeanUtils.copyProperties(feedbackSubmit, eFeedback);
            EFeedback saved = this.blogServ.saveFeedback(eFeedback, user.getId());
            return Result.success(saved);
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/blog/feedback/list/{type}/{pageNo}", method = RequestMethod.GET)
    public Object listFeedback(@PathVariable(name = "type") Integer type, @PathVariable(name = "pageNo") Integer pageNo) {
        try {
            return Result.success(this.blogServ.listPagedFeedback(type, pageNo));
        } catch (Exception e) {
            logger.error("", e);
            return Result.failWithExp(e);
        }
    }

}
