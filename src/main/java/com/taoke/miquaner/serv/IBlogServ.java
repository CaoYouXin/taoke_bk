package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EFeedback;
import com.taoke.miquaner.data.EHelpDoc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBlogServ {

    String storeBlog(String userId, String fileName, String content) throws IOException;

    String fetchBlog(String filePath, String domain) throws IOException;

    String uploadImage(String userId, MultipartFile image) throws IOException;

    String parseTitle(String content) throws IOException;

    EHelpDoc saveHelpDoc(EHelpDoc helpDoc);

    EFeedback saveFeedback(EFeedback feedback);

    void removeHelpDoc(Long id);

}
