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

    Object listAllUsers(Integer pageNo, Boolean showAnonymousFlag);

    boolean exportAll(String filePath, Boolean showAnonymousFlag);

    Object loginAnonymously(String hash);

    Object listAllNeedCheckUsers(Integer pageNo);

    boolean exportAllNeedCheck(String filePath);

    Object listTeamUsers(Long userId, Integer pageNo);

    boolean exportTeam(String filePath, Long userId);

    Object searchUsers(Integer pageNo, String search);

    boolean exportSearch(String filePath, String search);

    Object competeInfo(EUser user, UserRegisterSubmit userRegisterSubmit);

    Object getCustomerService(EUser user);

}
