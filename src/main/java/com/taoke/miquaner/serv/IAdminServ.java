package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EMenu;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;

public interface IAdminServ {

    Object setSuperUser(SuperUserSubmit superUserSubmit);

    Object getRoles();

    Object createAdmin(AdminUserSubmit adminUserSubmit);

    Object changeAdminRole(EAdmin admin);

    Object changeAdminPwd(EAdmin admin);

    Object createRole(ERole role);

    Object changeRole(ERole role);

    Object getPrivileges();

    Object bindPrivilege(BindSubmit bindSubmit);

    Object unbindPrivilege(BindSubmit bindSubmit);

    Object getMenus();

    Object createMenu(EMenu menu);

    Object changeMenu(EMenu menu);

    Object bindMenu(BindSubmit bindSubmit);

    Object unbindMenu(BindSubmit bindSubmit);

}
