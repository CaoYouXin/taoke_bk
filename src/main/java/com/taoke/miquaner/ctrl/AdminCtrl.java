package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.RoleSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

@RestController
public class AdminCtrl {

    @Autowired
    private IAdminServ adminServ;

    @RequestMapping("/admin/ping")
    public Object adminPing() {
        return Result.success("admin ping success");
    }

    @RequestMapping(value = "/admin/super/set", method = RequestMethod.POST)
    public Object setSuperUser(SuperUserSubmit superUserSubmit) {
        return this.adminServ.setSuperUser(superUserSubmit);
    }

    @RequestMapping("/admin/role/list")
    public Object getRoleList() {
        return this.adminServ.getRoles();
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public Object createAdmin(AdminUserSubmit adminUserSubmit) {
        return this.adminServ.createAdmin(adminUserSubmit);
    }

    @RequestMapping(value = "/admin/role/create", method = RequestMethod.POST)
    public Object createRole(RoleSubmit roleSubmit) {
        return this.adminServ.createRole(roleSubmit);
    }

    @RequestMapping("/admin/privilege/list")
    public Object getPrivileges() {
        return this.adminServ.getPrivileges();
    }

    @RequestMapping(value = "/admin/privilege/bind", method = RequestMethod.POST)
    public Object bindPrivilege(BindSubmit bindSubmit) {
        return this.adminServ.bindPrivilege(bindSubmit);
    }

    @RequestMapping(value = "/admin/privilege/unbind")
    public Object unbindPrivilege(BindSubmit bindSubmit) {
        return this.adminServ.unbindPrivilege(bindSubmit);
    }

    @RequestMapping(value = "/admin/role/change", method = RequestMethod.POST)
    public Object changeRole(ERole role) {
        return this.adminServ.changeRole(role);
    }

    @RequestMapping(value = "/admin/user/pwd/change", method = RequestMethod.POST)
    public Object changeAdminPwd(EAdmin admin) {
        return this.adminServ.changeAdminPwd(admin);
    }

    @RequestMapping(value = "/admin/user/role/change", method = RequestMethod.POST)
    public Object changeAdminRole(EAdmin admin) {
        return this.adminServ.changeAdminRole(admin);
    }

}
