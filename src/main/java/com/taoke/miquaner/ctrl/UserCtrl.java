package com.taoke.miquaner.ctrl;

import com.mysql.jdbc.StringUtils;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IUserServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.EnrollSubmit;
import com.taoke.miquaner.view.PhoneVerifySubmit;
import com.taoke.miquaner.view.UserRegisterSubmit;
import com.taoke.miquaner.view.UserResetPwdSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@RestController
public class UserCtrl {

    private final IUserServ userServ;

    @Autowired
    public UserCtrl(IUserServ userServ) {
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

    @Auth
    @RequestMapping(value = "/tbk/user/canWithdraw", method = RequestMethod.GET)
    public Object canWithdraw(HttpServletRequest request) {
        EUser user = null;
        try {
            user = EUser.class.cast(request.getAttribute("user"));
        } catch (Exception e) {
            return Result.success(false);
        }
        return Result.success(!StringUtils.isNullOrEmpty(user.getAliPayId()));
    }

    @Auth
    @RequestMapping(value = "/tbk/user/competeInfo", method = RequestMethod.POST)
    public Object competeInfo(@RequestBody UserRegisterSubmit userRegisterSubmit, HttpServletRequest request) {
        EUser user = null;
        try {
            user = EUser.class.cast(request.getAttribute("user"));
        } catch (Exception e) {
            return Result.fail(new ErrorR(ErrorR.NO_USER_FOUND, ErrorR.NO_USER_FOUND_MSG));
        }
        return this.userServ.competeInfo(user, userRegisterSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/user/check/agent/{id}", method = RequestMethod.POST)
    public Object check4Agent(@PathVariable(name = "id") Long id, @RequestBody String pid) {
        return this.userServ.check(id, pid.replaceAll("\"", ""));
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

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/manage/user/list/{pageNo}/{showAnonymousFlag}", method = RequestMethod.GET)
    public Object listAllUsers(@PathVariable(name = "pageNo") Integer pageNo, @PathVariable("showAnonymousFlag") Integer showAnonymousFlag) {
        return this.userServ.listAllUsers(pageNo, showAnonymousFlag == 1);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/manage/user/need/check/list/{pageNo}", method = RequestMethod.GET)
    public Object listAllNeedCheckUsers(@PathVariable(name = "pageNo") Integer pageNo) {
        return this.userServ.listAllNeedCheckUsers(pageNo);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/manage/user/team/list/{pageNo}/of/{userId}", method = RequestMethod.GET)
    public Object listTeamUsers(@PathVariable(name = "pageNo") Integer pageNo, @PathVariable("userId") Long userId) {
        return this.userServ.listTeamUsers(userId, pageNo);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/manage/user/search/{search}/{pageNo}", method = RequestMethod.GET)
    public Object listTeamUsers(@PathVariable("search") String search, @PathVariable(name = "pageNo") Integer pageNo) {
        return this.userServ.searchUsers(pageNo, search);
    }

}
