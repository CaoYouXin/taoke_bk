package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.view.EnrollSubmit;
import com.taoke.miquaner.view.UserRegisterSubmit;
import com.taoke.miquaner.view.UserResetPwdSubmit;

import java.util.List;

public interface IUserServ {

    Object login(EUser user);

    Object register(UserRegisterSubmit userRegisterSubmit);

    Object resetPwd(UserResetPwdSubmit userResetPwdSubmit);

    Object sendVerifyCode(String phone);

    Object enroll(EUser user, EnrollSubmit enrollSubmit);

    Object check(Long id, String aliPid);

    Object downGrade(Long id);

    List<EUser> getChildUsers(EUser user);

    Object listAllUsers(Integer pageNo);

    boolean exportAll(String filePath);

}
