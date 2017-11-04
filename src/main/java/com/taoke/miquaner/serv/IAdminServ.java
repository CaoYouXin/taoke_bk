package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.RoleSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;

public interface IAdminServ {

    Object setSuperUser(SuperUserSubmit superUserSubmit);

    Object getRoles();

    Object createAdmin(AdminUserSubmit adminUserSubmit);

    Object changeAdminRole(EAdmin admin);

    Object changeAdminPwd(EAdmin admin);

    Object createRole(RoleSubmit roleSubmit);

    Object changeRole(ERole role);

    Object getPrivileges();

    Object bindPrivilege(BindSubmit bindSubmit);

    Object unbindPrivilege(BindSubmit bindSubmit);


}
