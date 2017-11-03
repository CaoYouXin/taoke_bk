package com.taoke.miquaner.serv;

import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.RoleSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;

public interface IAdminServ {

    Object setSuperUser(SuperUserSubmit superUserSubmit);

    Object getRoles();

    Object createAdmin(AdminUserSubmit adminUserSubmit);

    Object createRole(RoleSubmit roleSubmit);

    Object getPrivileges();

}
