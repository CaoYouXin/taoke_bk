package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.fltr.AdminInterceptor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Controller
public class FileCtrl {

    private static final Logger logger = LogManager.getLogger(FileCtrl.class);

    private Environment env;

    @Autowired
    public FileCtrl(Environment env) {
        this.env = env;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("uploadFiles") MultipartFile[] uploadingFiles) {

        String directory = env.getProperty("taoke.paths.uploadedFiles");
        try {
            for(MultipartFile uploadedFile : uploadingFiles) {
                String filename = uploadedFile.getOriginalFilename();
                String filepath = Paths.get(directory, filename).toString();
                File file = new File(filepath);
                uploadedFile.transferTo(file);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            logger.error("上传文件发生I/O错误");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
