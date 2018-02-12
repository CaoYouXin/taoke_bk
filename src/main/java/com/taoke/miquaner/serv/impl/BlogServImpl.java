package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EFeedback;
import com.taoke.miquaner.data.EHelpDoc;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.FeedbackRepo;
import com.taoke.miquaner.repo.HelpDocRepo;
import com.taoke.miquaner.repo.UserRepo;
import com.taoke.miquaner.serv.IBlogServ;
import com.taoke.miquaner.serv.exp.MdParseException;
import com.taoke.miquaner.serv.exp.SearchTypeException;
import com.taoke.miquaner.util.BeanUtil;
import com.taoke.miquaner.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlogServImpl implements IBlogServ {

    private final static Pattern PATTERN = Pattern.compile("!\\[(?<alt>.*?)]\\((?<imageUrl>.*?)\\)");

    private final UserRepo userRepo;
    private final HelpDocRepo helpDocRepo;
    private final FeedbackRepo feedbackRepo;
    private final String BlogRoot;
    private final String BlogPosts;
    private final String BlogImages;
    private Converter<EFeedback, EFeedback> feedbackConverter = feedback -> {
        EUser user = feedback.getUser();
        EUser view = new EUser();
        view.setName(user.getName() + "-" + user.getPhone());
        feedback.setUser(view);
        return feedback;
    };;

    @Autowired
    public BlogServImpl(UserRepo userRepo, Environment env, HelpDocRepo helpDocRepo, FeedbackRepo feedbackRepo) {
        this.userRepo = userRepo;
        this.helpDocRepo = helpDocRepo;
        this.feedbackRepo = feedbackRepo;

        this.BlogRoot = env.getProperty("taoke.blog.root");
        this.BlogPosts = env.getProperty("taoke.blog.posts");
        this.BlogImages = env.getProperty("taoke.blog.images");
    }

    @Override
    public String storeBlog(String userId, String fileName, String content) throws IOException {
        String filePath = userId + BlogPosts + fileName + ".md";
        File file = new File(BlogRoot + filePath);
        this.makeFileExist(file);
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            out.writeUTF(content);
            out.flush();
            out.close();
        }
        return filePath;
    }

    @Override
    public String fetchBlog(String filePath, String domain) throws IOException {
        String content = "";
        try (DataInputStream in = new DataInputStream(new FileInputStream(
                BlogRoot + (filePath.endsWith(".md") ? filePath : filePath + ".md")
        ))) {
            content = in.readUTF();
        }

        Matcher m0 = PATTERN.matcher(content);
        StringBuffer sb0 = new StringBuffer();

        while (m0.find()) {
            String alt = m0.group("alt");
            String imageUrl = m0.group("imageUrl");

            if (!imageUrl.startsWith("http")) {
                imageUrl = domain + imageUrl;
            }
            m0.appendReplacement(sb0, String.format("![%s](%s)", alt, imageUrl.replaceAll(" ", "%20")));
        }
        m0.appendTail(sb0);

        return sb0.toString();
    }

    @Override
    public String uploadImage(String userId, MultipartFile image) throws IOException {
        String filePath = userId + BlogImages + StringUtils.randomMd5()
                + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.'));
        File file = new File(BlogRoot + filePath);
        this.makeFileExist(file);
        image.transferTo(file);
        return filePath;
    }

    @Override
    public String parseTitle(String content) throws IOException {
        StringReader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);
        String firstLine = br.readLine();
        if (!"---".equals(firstLine)) {
            throw new MdParseException("first line should be '---'");
        }

        String line = null;
        do {
            line = br.readLine();
            if (null == line) {
                break;
            }

            int splitCharIdx = line.indexOf(':');
            if (-1 == splitCharIdx) {
                throw new MdParseException("there should be a ':' to split config key and value");
            }

            String key = line.substring(0, splitCharIdx).trim();
            if ("title".equalsIgnoreCase(key)) {
                return line.substring(splitCharIdx + 1).trim();
            }
        } while (!"---".equals(line));

        throw new MdParseException("no title config found");
    }

    @Override
    public String dryMarkdown(String content) throws IOException {
        StringReader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);
        String firstLine = br.readLine();
        if (!"---".equals(firstLine)) {
            return content;
        }

        String line = null;
        do {
            line = br.readLine();
            if (null == line) {
                throw new MdParseException("config not properly end");
            }
        } while (!"---".equals(line));

        StringBuilder sb = new StringBuilder();
        while (true) {
            line = br.readLine();
            if (null == line) {
                return sb.toString();
            }

            sb.append(line).append('\n');
        }
    }

    @Override
    public EHelpDoc saveHelpDoc(EHelpDoc helpDoc) {
        if (null != helpDoc.getId()) {
            EHelpDoc one = this.helpDocRepo.findOne(helpDoc.getId());
            BeanUtil.copyNotNullProps(helpDoc, one);
            helpDoc = one;
        }

        return this.helpDocRepo.save(helpDoc);
    }

    @Override
    public EFeedback saveFeedback(EFeedback feedback, Long userId) {
        if (null != feedback.getId()) {
            EFeedback one = this.feedbackRepo.findOne(feedback.getId());
            BeanUtil.copyNotNullProps(feedback, one);
            feedback = one;
        }

        if (null == feedback.getCreateTime()) {
            feedback.setCreateTime(new Date());
        }

        if (feedback.getChecked() && null == feedback.getCheckTime()) {
            feedback.setCheckTime(new Date());
        }

        feedback.setUser(this.userRepo.findOne(userId));

        EFeedback saved = this.feedbackRepo.save(feedback);
        saved.setUser(null);

        return saved;
    }

    @Override
    public void removeHelpDoc(Long id) {
        this.helpDocRepo.delete(id);
    }

    @Override
    public List<EHelpDoc> listAllHelpDoc() {
        return this.helpDocRepo.findAllByOrderByOrderDesc();
    }

    @Override
    public List<EHelpDoc> listHelpDocByType(Integer type) {
        return this.helpDocRepo.findAllByTypeEqualsOrderByOrderDesc(type);
    }

    @Override
    public Page<EFeedback> listPagedFeedback(Integer type, Integer pageNo) throws SearchTypeException {
        pageNo = Math.max(0, --pageNo);
        PageRequest pageRequest = new PageRequest(pageNo, 10, Sort.Direction.DESC, "createTime");
        switch (type) {
            case 1:
                return this.feedbackRepo.findAll(pageRequest).map(feedbackConverter);
            case 2:
                return this.feedbackRepo.findAllByCheckedEquals(true, pageRequest).map(feedbackConverter);
            case 3:
                return this.feedbackRepo.findAllByCheckedEquals(false, pageRequest).map(feedbackConverter);
        }

        throw new SearchTypeException("type [" + type + "] is not recognized");
    }

    private void makeFileExist(File file) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IOException("can not make dirs");
                }
            }

            if (!file.createNewFile()) {
                throw new IOException("can not make new file");
            }
        }
    }
}
