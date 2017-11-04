package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EMenu;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminCtrl {

    private IAdminServ adminServ;

    @Autowired
    public AdminCtrl(IAdminServ adminServ) {
        this.adminServ = adminServ;
    }

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
    public Object createRole(ERole role) {
        return this.adminServ.createRole(role);
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

    @RequestMapping("/admin/menu/list")
    public Object getMenuList() {
        return this.adminServ.getMenus();
    }

    @RequestMapping(value = "/admin/menu/create", method = RequestMethod.POST)
    public Object createMenu(EMenu menu) {
        return this.adminServ.createMenu(menu);
    }

    @RequestMapping(value = "/admin/menu/change", method = RequestMethod.POST)
    public Object changeMenu(EMenu menu) {
        return this.adminServ.changeMenu(menu);
    }

    @RequestMapping(value = "/admin/menu/bind", method = RequestMethod.POST)
    public Object bindMenu(BindSubmit bindSubmit) {
        return this.adminServ.bindMenu(bindSubmit);
    }

    @RequestMapping(value = "/admin/menu/unbind", method = RequestMethod.POST)
    public Object unbindMenu(BindSubmit bindSubmit) {
        return this.adminServ.unbindMenu(bindSubmit);
    }

    @RequestMapping("/admin/menu/delete/{id}")
    public Object deleteMenu(@PathVariable(name = "id") Long id) {
        return this.adminServ.deleteMenu(id);
    }

}
