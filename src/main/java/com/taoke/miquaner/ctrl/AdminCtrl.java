package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EMenu;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminCtrl {

    private IAdminServ adminServ;

    @Autowired
    public AdminCtrl(IAdminServ adminServ) {
        this.adminServ = adminServ;
    }

    @Auth(isAdmin = true)
    @RequestMapping("/admin/ping")
    public Object adminPing() {
        return Result.success("admin ping success");
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/super/set", method = RequestMethod.POST)
    public Object setSuperUser(SuperUserSubmit superUserSubmit) {
        return this.adminServ.setSuperUser(superUserSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/admin/role/list")
    public Object getRoleList() {
        return this.adminServ.getRoles();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public Object createAdmin(AdminUserSubmit adminUserSubmit, HttpServletRequest request) {
        return this.adminServ.createAdmin(adminUserSubmit, (EAdmin) request.getAttribute("admin"));
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/role/create", method = RequestMethod.POST)
    public Object createRole(ERole role) {
        return this.adminServ.createRole(role);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/admin/privilege/list")
    public Object getPrivileges() {
        return this.adminServ.getPrivileges();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/privilege/bind", method = RequestMethod.POST)
    public Object bindPrivilege(BindSubmit bindSubmit) {
        return this.adminServ.bindPrivilege(bindSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/privilege/unbind")
    public Object unbindPrivilege(BindSubmit bindSubmit) {
        return this.adminServ.unbindPrivilege(bindSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/role/change", method = RequestMethod.POST)
    public Object changeRole(ERole role) {
        return this.adminServ.changeRole(role);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/user/pwd/change", method = RequestMethod.POST)
    public Object changeAdminPwd(EAdmin admin, HttpServletRequest request) {
        return this.adminServ.changeAdminPwd(admin, (EAdmin) request.getAttribute("admin"));
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/user/role/change", method = RequestMethod.POST)
    public Object changeAdminRole(EAdmin admin, HttpServletRequest request) {
        return this.adminServ.changeAdminRole(admin, (EAdmin) request.getAttribute("admin"));
    }

    @Auth(isAdmin = true)
    @RequestMapping("/admin/menu/list")
    public Object getMenuList() {
        return this.adminServ.getMenus();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/menu/create", method = RequestMethod.POST)
    public Object createMenu(EMenu menu) {
        return this.adminServ.createMenu(menu);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/menu/change", method = RequestMethod.POST)
    public Object changeMenu(EMenu menu) {
        return this.adminServ.changeMenu(menu);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/menu/bind", method = RequestMethod.POST)
    public Object bindMenu(BindSubmit bindSubmit) {
        return this.adminServ.bindMenu(bindSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/menu/unbind", method = RequestMethod.POST)
    public Object unbindMenu(BindSubmit bindSubmit) {
        return this.adminServ.unbindMenu(bindSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/menu/delete/{id}", method = RequestMethod.POST)
    public Object deleteMenu(@PathVariable(name = "id") Long id) {
        return this.adminServ.deleteMenu(id);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/admin/user/delete/{id}", method = RequestMethod.POST)
    public Object deleteAdmin(@PathVariable(name = "id") Long id) {
        return this.adminServ.deleteAdmin(id);
    }

    @RequestMapping(value = "/admin/user/login", method = RequestMethod.POST)
    public Object login(EAdmin admin) {
        return this.adminServ.adminLogin(admin);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/admin/user/list")
    public Object listAdmins(HttpServletRequest request) {
        return this.adminServ.listAdmins((EAdmin) request.getAttribute("admin"));
    }
}
