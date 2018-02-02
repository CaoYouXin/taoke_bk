package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EFeedback;
import com.taoke.miquaner.data.EHelp;
import com.taoke.miquaner.data.EHelpDoc;
import com.taoke.miquaner.serv.exp.SearchTypeException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBlogServ {

    String storeBlog(String userId, String content) throws IOException;

    String fetchBlog(String filePath, String domain) throws IOException;

    String uploadImage(String userId, MultipartFile image) throws IOException;

    String parseTitle(String content) throws IOException;

    EHelpDoc saveHelpDoc(EHelpDoc helpDoc);

    EFeedback saveFeedback(EFeedback feedback);

    void removeHelpDoc(Long id);

    List<EHelpDoc> listAllHelpDoc();

    Page<EFeedback> listPagedFeedback(Integer type, Integer pageNo) throws SearchTypeException;

}
