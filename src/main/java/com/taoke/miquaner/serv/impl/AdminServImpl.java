package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.*;
import com.taoke.miquaner.repo.*;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminServImpl implements IAdminServ {

    private static final String ST_NOT_MATCH = "你的Token有误";
    private static final String NO_SUPER_ROLE = "没有超管权限可以绑定，错误可能发生在启动服务时";
    private static final String ALREADY_HAS_SUPER_USER = "系统里已经存在一个超级管理员了，超级管理员只能存在一个";
    private static final String SUBMIT_NEED_ROLE = "未指定角色";
    private static final String ALREADY_BIND = "已经绑定过";
    private static final String BIND_SUCCESS = "绑定成功";
    private static final String UNBIND_SUCCESS = "解绑成功";
    private static final String NO_ID_FOUND = "没有找到主键，错误可能发生在前端漏传主键字段";

    private ConfigRepo configRepo;
    private AdminRepo adminRepo;
    private RoleRepo roleRepo;
    private PrivilegeRepo privilegeRepo;
    private MenuRepo menuRepo;

    @Autowired
    public AdminServImpl(ConfigRepo configRepo, AdminRepo adminRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, MenuRepo menuRepo) {
        this.configRepo = configRepo;
        this.adminRepo = adminRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.menuRepo = menuRepo;
    }

    @Override
    public Object setSuperUser(SuperUserSubmit superUserSubmit) {
        EConfig checkSt = this.configRepo.findByKeyEqualsAndAndValueEquals(EConfig.SERVER_TOKEN, superUserSubmit.getSt());
        if (null == checkSt) {
            return Result.fail(new ErrorR(ErrorR.ST_NOT_MATCH, ST_NOT_MATCH));
        }

        ERole superRole = this.roleRepo.findByNameEquals(ERole.SUPER_ROLE_NAME);
        if (null == superRole) {
            return Result.fail(new ErrorR(ErrorR.NO_SUPER_ROLE, NO_SUPER_ROLE));
        }

        if (!superRole.getAdmins().isEmpty()) {
            return Result.fail(new ErrorR(ErrorR.ALREADY_HAS_SUPER_USER, ALREADY_HAS_SUPER_USER));
        }

        EAdmin admin = new EAdmin();
        BeanUtils.copyProperties(superUserSubmit, admin);
        admin.setRole(superRole);
        this.adminRepo.save(admin);
        admin.setGrantedAdmins(null);
        admin.getParentAdmin().setGrantedAdmins(null);
        return Result.success(admin);
    }

    @Override
    public Object getRoles() {
        List<ERole> all = this.roleRepo.findAll();
        return Result.success(all.stream().filter(eRole -> !eRole.isSuperRole()).peek(eRole -> {
            eRole.setAdmins(null);
            eRole.setMenus(null);
            eRole.setPrivileges(null);
        }).collect(Collectors.toList()));
    }

    @Override
    public Object createAdmin(AdminUserSubmit adminUserSubmit) {
        ERole role = this.roleRepo.findOne(adminUserSubmit.getRoleId());
        if (null == role) {
            return Result.fail(new ErrorR(ErrorR.SUBMIT_NEED_ROLE, SUBMIT_NEED_ROLE));
        }

        EAdmin admin = new EAdmin();
        BeanUtils.copyProperties(adminUserSubmit, admin);
        admin.setRole(role);
        this.adminRepo.save(admin);
        admin.setGrantedAdmins(null);
        admin.getParentAdmin().setGrantedAdmins(null);
        return Result.success(admin);
    }

    @Override
    public Object changeAdminRole(EAdmin admin) {
        return this.persistentNewAdmin(admin);
    }

    @Override
    public Object changeAdminPwd(EAdmin admin) {
        return this.persistentNewAdmin(admin);
    }

    private Object persistentNewAdmin(EAdmin admin) {
        if (null == admin.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        EAdmin one = this.adminRepo.findOne(admin.getId());
        BeanUtils.copyProperties(admin, one);
        this.adminRepo.save(admin);
        admin.setGrantedAdmins(null);
        admin.getParentAdmin().setGrantedAdmins(null);
        return Result.success(admin);
    }

    @Override
    public Object createRole(ERole role) {
        this.roleRepo.save(role);
        role.setPrivileges(null);
        role.setMenus(null);
        role.setAdmins(null);
        return Result.success(role);
    }

    @Override
    public Object changeRole(ERole role) {
        if (null == role.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        ERole one = this.roleRepo.findOne(role.getId());
        BeanUtils.copyProperties(role, one);
        this.roleRepo.save(role);
        role.setPrivileges(null);
        role.setMenus(null);
        role.setAdmins(null);
        return Result.success(role);
    }

    @Override
    public Object getPrivileges() {
        return Result.success(this.privilegeRepo.findAll().stream().peek(ePrivilege -> ePrivilege.setRoles(null)).collect(Collectors.toList()));
    }

    @Override
    public Object bindPrivilege(BindSubmit bindSubmit) {
        EPrivilege privilege = this.privilegeRepo.findOne(bindSubmit.getId());
        boolean alreadyBind = privilege.getRoles().stream().anyMatch(eRole -> bindSubmit.getTo().equals(eRole.getId()));

        if (alreadyBind) {
            return Result.success(ALREADY_BIND);
        }

        ERole role = this.roleRepo.findOne(bindSubmit.getTo());
        privilege.getRoles().add(role);
        this.privilegeRepo.save(privilege);
        return Result.success(BIND_SUCCESS);
    }

    @Override
    public Object unbindPrivilege(BindSubmit bindSubmit) {
        EPrivilege privilege = this.privilegeRepo.findOne(bindSubmit.getId());
        privilege.setRoles(privilege.getRoles().stream().filter(eRole -> !bindSubmit.getTo().equals(eRole.getId())).collect(Collectors.toList()));
        this.privilegeRepo.save(privilege);
        return Result.success(UNBIND_SUCCESS);
    }

    @Override
    public Object getMenus() {
        return Result.success(this.menuRepo.findAll().stream().peek(menu -> menu.setRoles(null)).collect(Collectors.toList()));
    }

    @Override
    public Object createMenu(EMenu menu) {
        this.menuRepo.save(menu);
        menu.setRoles(null);
        return Result.success(menu);
    }

    @Override
    public Object changeMenu(EMenu menu) {
        if (null == menu.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        EMenu one = this.menuRepo.findOne(menu.getId());
        BeanUtils.copyProperties(menu, one);
        this.menuRepo.save(menu);
        menu.setRoles(null);
        return Result.success(menu);
    }

    @Override
    public Object deleteMenu(Long id) {
        this.menuRepo.delete(id);
        return Result.success(Result.SUCCESS_MSG);
    }

    @Override
    public Object bindMenu(BindSubmit bindSubmit) {
        EMenu menu = this.menuRepo.findOne(bindSubmit.getId());
        boolean alreadyBind = menu.getRoles().stream().anyMatch(role -> bindSubmit.getTo().equals(role.getId()));

        if (alreadyBind) {
            return Result.success(ALREADY_BIND);
        }

        ERole role = this.roleRepo.findOne(bindSubmit.getTo());
        menu.getRoles().add(role);
        this.menuRepo.save(menu);
        return Result.success(BIND_SUCCESS);
    }

    @Override
    public Object unbindMenu(BindSubmit bindSubmit) {
        EMenu menu = this.menuRepo.findOne(bindSubmit.getId());
        menu.setRoles(menu.getRoles().stream().filter(eRole -> !bindSubmit.getTo().equals(eRole.getId())).collect(Collectors.toList()));
        this.menuRepo.save(menu);
        return Result.success(UNBIND_SUCCESS);
    }
}
