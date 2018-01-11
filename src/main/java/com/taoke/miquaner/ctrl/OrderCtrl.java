package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IOrderServ;
import com.taoke.miquaner.serv.IUserServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

@RestController
public class OrderCtrl {

    private final Environment env;
    private final IOrderServ orderServ;
    private final IUserServ userServ;

    @Autowired
    public OrderCtrl(Environment env, IOrderServ orderServ, IUserServ userServ) {
        this.env = env;
        this.orderServ = orderServ;
        this.userServ = userServ;
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/order/upload", method = RequestMethod.POST)
    public Object uploadOrder(@RequestBody String fileName) throws IOException {
        String directory = env.getProperty("taoke.paths.uploadedFiles");
        return this.orderServ.upload(Paths.get(directory, fileName.replaceAll("\"", "")).toString());
    }

    @Auth
    @RequestMapping(value = "/tbk/order/list/{type}/{pageNo}", method = RequestMethod.GET)
    public Object listOrders(@PathVariable(name = "type") Integer type, @PathVariable(name = "pageNo") Integer pageNo, HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.success(Collections.emptyList());
        }
        return this.orderServ.list((EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"), type, pageNo);
    }

    @Auth
    @RequestMapping(value = "/tbk/team/list", method = RequestMethod.GET)
    public Object getTeamList(HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.success(Collections.emptyList());
        }
        return this.orderServ.getChildUserCommit(this.userServ.getChildUsers((EUser) request.getAttribute("user")));
    }

    @Auth
    @RequestMapping(value = "/tbk/candraw", method = RequestMethod.GET)
    public Object getCandraw(HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.success("0.00");
        }
        return this.orderServ.canDraw((EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/estimate/this", method = RequestMethod.GET)
    public Object getThisEstimate(HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.success("0.00");
        }
        return this.orderServ.thisMonthSettled((EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/estimate/that", method = RequestMethod.GET)
    public Object getLastEstimate(HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.success("0.00");
        }
        return this.orderServ.lastMonthSettled((EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/withdraw/{amount}", method = RequestMethod.GET)
    public Object withdraw(@PathVariable(name = "amount") Double amount, HttpServletRequest request) {
        Boolean buyer = (Boolean) request.getAttribute("buyer");
        if (buyer) {
            return Result.fail(new ErrorR(ErrorR.NOT_FOR_BUYER, ErrorR.NOT_FOR_BUYER_MSG));
        }
        return this.orderServ.withdraw((EUser) request.getAttribute("user"), amount, (Boolean) request.getAttribute("super"));
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/withdraw/request/list", method = RequestMethod.GET)
    public Object withdrawList() {
        return this.orderServ.userWithdrawList();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/withdraw/response/{id}", method = RequestMethod.GET)
    public Object payWithdraw(@PathVariable(name = "id") Long id) {
        return this.orderServ.payUserWithdraw(id);
    }

}
