package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.serv.IOrderServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class OrderCtrl {

    private Environment env;
    private IOrderServ orderServ;

    @Autowired
    public OrderCtrl(Environment env, IOrderServ orderServ) {
        this.env = env;
        this.orderServ = orderServ;
    }

    @RequestMapping("/tbk/order/upload")
    public Object uploadOrder() throws IOException {
        String directory = env.getProperty("taoke.paths.uploadedFiles");
        String fileName = "TaokeDetail-2017-11-08.xls";

        return this.orderServ.upload(Paths.get(directory, fileName).toString());
    }

}
