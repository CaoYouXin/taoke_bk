package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IOrderServ;
import com.taoke.miquaner.serv.IUserServ;
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
    private IUserServ userServ;

    @Autowired
    public OrderCtrl(Environment env, IOrderServ orderServ, IUserServ userServ) {
        this.env = env;
        this.orderServ = orderServ;
        this.userServ = userServ;
    }

    @Auth(isAdmin = true)
    @RequestMapping("/tbk/order/upload")
    public Object uploadOrder(String fileName) throws IOException {
        String directory = env.getProperty("taoke.paths.uploadedFiles");
        return this.orderServ.upload(Paths.get(directory, fileName).toString());
    }

    @Auth
    @RequestMapping("/tbk/order/list/{type}/{pageNo}")
    public Object listOrders(@PathVariable(name = "type") Integer type, @PathVariable(name = "pageNo") Integer pageNo, HttpServletRequest request) {
        return this.orderServ.list((EUser) request.getAttribute("user"), type, pageNo);
    }

    @Auth
    @RequestMapping("/tbk/team/list")
    public Object getTeamList(HttpServletRequest request) {
        return this.orderServ.getChildUserCommit(this.userServ.getChildUsers((EUser) request.getAttribute("user")));
    }

    @Auth
    @RequestMapping("/tbk/candraw")
    public Object getCandraw(HttpServletRequest request) {
        return this.orderServ.canDraw((EUser) request.getAttribute("user"));
    }

    @Auth
    @RequestMapping("/tbk/estimate/this")
    public Object getThisEstimate(HttpServletRequest request) {
        return this.orderServ.thisMonthSettled((EUser) request.getAttribute("user"));
    }

    @Auth
    @RequestMapping("/tbk/estimate/that")
    public Object getLastEstimate(HttpServletRequest request) {
        return this.orderServ.lastMonthSettled((EUser) request.getAttribute("user"));
    }

    @Auth
    @RequestMapping("/tbk/withdraw/{amount}")
    public Object withdraw(@PathVariable(name = "amount") Double amount, HttpServletRequest request) {
        return this.orderServ.withdraw((EUser) request.getAttribute("user"), amount);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/tbk/withdraw/request/list")
    public Object withdrawList() {
        return this.orderServ.userWithdrawList();
    }

    @Auth(isAdmin = true)
    @RequestMapping("/tbk/withdraw/response/{id}")
    public Object payWithdraw(@PathVariable(name = "id") Long id) {
        return this.orderServ.payUserWithdraw(id);
    }

}
