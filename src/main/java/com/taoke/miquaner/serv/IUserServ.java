package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.view.UserRegisterSubmit;

public interface IUserServ {

    Object login(EUser user);

    Object register(UserRegisterSubmit userRegisterSubmit);

}
