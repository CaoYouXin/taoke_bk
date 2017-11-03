package com.taoke.miquaner.serv;

import com.taoke.miquaner.view.SuperUserSubmit;

public interface IAdminServ {

    Object setSuperUser(SuperUserSubmit superUserSubmit);

    Object getRoles();

}
