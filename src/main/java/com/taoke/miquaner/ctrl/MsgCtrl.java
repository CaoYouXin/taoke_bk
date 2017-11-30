package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IMsgServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.view.MessageSubmit;
import com.taoke.miquaner.view.ReportSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MsgCtrl {

    private IMsgServ msgServ;

    @Autowired
    public MsgCtrl(IMsgServ msgServ) {
        this.msgServ = msgServ;
    }

    @Auth
    @RequestMapping("/msg/list/{pageNo}")
    public Object getMsgList(@PathVariable(name = "pageNo") Integer pageNo, HttpServletRequest request) {
        return this.msgServ.listMessages((EUser) request.getAttribute("user"), pageNo);
    }

    @Auth
    @RequestMapping("/msg/unread/count")
    public Object getUnreadCount(HttpServletRequest request) {
        return this.msgServ.getUncheckedMessageCount((EUser) request.getAttribute("user"));
    }

    @Auth
    @RequestMapping("/msg/read/{id}")
    public Object readMessage(HttpServletRequest request, @PathVariable(name = "id") Long id) {
        return this.msgServ.readMessage((EUser) request.getAttribute("user"), id);
    }

    @Auth
    @RequestMapping(value = "/msg/feedback", method = RequestMethod.POST)
    public Object feedback(HttpServletRequest request, @RequestBody ReportSubmit content) {
        return this.msgServ.sendFeedback((EUser) request.getAttribute("user"), content.getReport().replaceAll("\"", ""));
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/msg/send/2/all", method = RequestMethod.POST)
    public Object send2All(@RequestBody MessageSubmit messageSubmit, HttpServletRequest request) {
        return this.msgServ.send2All((EAdmin) request.getAttribute("admin"), messageSubmit.getTitle(), messageSubmit.getContent());
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/msg/feedback", method = RequestMethod.GET)
    public Object readFeedback() {
        return this.msgServ.readFeedback();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/msg/send/2/all/read", method = RequestMethod.GET)
    public Object getSend2All(HttpServletRequest request) {
        return this.msgServ.getSend2All((EAdmin) request.getAttribute("admin"));
    }

}
