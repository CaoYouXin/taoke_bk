package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EHelpDoc;
import com.taoke.miquaner.serv.IBlogServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.HelpDocSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class BlogCtrl {

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
            String filePath = this.blogServ.storeBlog(BlogAdmin, helpDocSubmit.getContent());
            helpDocSubmit.setPath(filePath);
            EHelpDoc eHelpDoc = this.blogServ.saveHelpDoc(helpDocSubmit);
            return Result.success(eHelpDoc);
        } catch (Exception e) {
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
            return Result.failWithExp(e);
        }
    }

}
