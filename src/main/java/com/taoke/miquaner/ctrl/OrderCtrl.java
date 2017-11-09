package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IOrderServ;
import com.taoke.miquaner.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Auth
    @RequestMapping("/tbk/order/list/{type}/{pageNo}")
    public Object listOrders(@PathVariable(name = "type") Integer type, @PathVariable(name = "pageNo") Integer pageNo, HttpServletRequest request) {
        return this.orderServ.list((EUser) request.getAttribute("user"), type, pageNo);
    }

}
