package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IUserServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.view.EnrollSubmit;
import com.taoke.miquaner.view.PhoneVerifySubmit;
import com.taoke.miquaner.view.UserRegisterSubmit;
import com.taoke.miquaner.view.UserResetPwdSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@RestController
public class UserCtrl {

    private Environment env;
    private IUserServ userServ;

    @Autowired
    public UserCtrl(Environment env, IUserServ userServ) {
        this.env = env;
        this.userServ = userServ;
    }

    @RequestMapping(value = "/tbk/user/login", method = RequestMethod.POST)
    public Object login(@RequestBody EUser user) {
        return this.userServ.login(user);
    }

    @RequestMapping(value = "/tbk/user/register", method = RequestMethod.POST)
    public Object register(Reader reader) throws IOException {
        return this.userServ.register(
                MiquanerApplication.DEFAULT_OBJECT_MAPPER.readValue(
                        new BufferedReader(reader).readLine(),
                        UserRegisterSubmit.class
                )
        );
    }

    @RequestMapping(value = "/tbk/phone/verify", method = RequestMethod.POST)
    public Object verify(@RequestBody PhoneVerifySubmit phoneVerifySubmit) {
        return this.userServ.sendVerifyCode(phoneVerifySubmit.getPhone());
    }

    @RequestMapping(value = "/tbk/user/reset/pwd", method = RequestMethod.POST)
    public Object resetPwd(@RequestBody UserResetPwdSubmit userResetPwdSubmit) {
        return this.userServ.resetPwd(userResetPwdSubmit);
    }

    @Auth
    @RequestMapping(value = "/tbk/user/apply/4/agent", method = RequestMethod.POST)
    public Object apply4Agent(@RequestBody EnrollSubmit enrollSubmit, HttpServletRequest request) {
        return this.userServ.enroll((EUser) request.getAttribute("user"), enrollSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/user/check/agent/{id}", method = RequestMethod.POST)
    public Object check4Agent(@PathVariable(name = "id") Long id, @RequestBody String pid) {
        return this.userServ.check(id, pid.replaceAll("\"", ""));
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/manage/user/list/{pageNo}", method = RequestMethod.GET)
    public Object listAllUsers(@PathVariable(name = "pageNo") Integer pageNo) {
        return this.userServ.listAllUsers(pageNo);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/user/down/grade/{id}", method = RequestMethod.GET)
    public Object downGrade(@PathVariable(name = "id") Long id) {
        return this.userServ.downGrade(id);
    }

    @RequestMapping(value = "/tbk/user/anonymous/{hash}", method = RequestMethod.GET)
    public Object anonymous(@PathVariable(name = "hash") String hash) {
        return this.userServ.loginAnonymously(hash);
    }

}
