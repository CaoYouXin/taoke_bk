package com.taoke.miquaner.ctrl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.fltr.AdminInterceptor;
import com.taoke.miquaner.util.Auth;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileCtrl {

    private static final Logger logger = LogManager.getLogger(FileCtrl.class);

    private Environment env;

    @Autowired
    public FileCtrl(Environment env) {
        this.env = env;
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("uploadFiles") MultipartFile[] uploadingFiles) {

        String directory = env.getProperty("taoke.paths.uploadedFiles");
        try {
            Map<String, String> ret = new HashMap<>();
            for(MultipartFile uploadedFile : uploadingFiles) {
                String filename = StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(new Date()))
                        + uploadedFile.getOriginalFilename().substring(uploadedFile.getOriginalFilename().lastIndexOf('.'));
                String filepath = Paths.get(directory, filename).toString();
                File file = new File(filepath);
                uploadedFile.transferTo(file);
                ret.put(uploadedFile.getOriginalFilename(), filename);
            }
            return new ResponseEntity<>(MiquanerApplication.DEFAULT_OBJECT_MAPPER.writeValueAsString(ret), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("上传文件发生I/O错误");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
