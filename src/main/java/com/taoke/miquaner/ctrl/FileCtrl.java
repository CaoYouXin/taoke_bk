package com.taoke.miquaner.ctrl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.serv.IUserServ;
import com.taoke.miquaner.util.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileCtrl {

    private static final Logger logger = LogManager.getLogger(FileCtrl.class);

    private Environment env;
    private IUserServ userServ;

    @Autowired
    public FileCtrl(Environment env, IUserServ userServ) {
        this.env = env;
        this.userServ = userServ;
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

    @Auth(isAdmin = true)
    @RequestMapping(value = "/export/all/2", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> exportAll()
            throws IOException {
        String directory = env.getProperty("taoke.paths.uploadedFiles");
        String fileName = String.format("ALL-USERS_%s.xls", MiquanerApplication.DEFAULT_DATE_FORMAT.format(new Date()));
        String filePath = Paths.get(directory, fileName).toString();

        boolean suc = this.userServ.exportAll(filePath);
        if (!suc) {
            return ResponseEntity.status(500).body(null);
        }

        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("fileName", fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(file.getInputStream()));
    }

}
