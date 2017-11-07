package com.taoke.miquaner.serv.impl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.*;
import com.taoke.miquaner.repo.*;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.DateUtils;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminLoginView;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private static final String SUBMIT_NEED_NAME = "未指定用户名";
    private static final String ADMIN_NOT_FOUND = "没有该管理员";
    private static final String ADMIN_WRONG_PWD = "管理员密码错误，请联系您的上级管理员";
    private static final String ADMIN_NOT_PERMITTED = "该管理员并非您的权限范围";

    private ConfigRepo configRepo;
    private AdminRepo adminRepo;
    private RoleRepo roleRepo;
    private PrivilegeRepo privilegeRepo;
    private MenuRepo menuRepo;
    private TokenRepo tokenRepo;

    @Autowired
    public AdminServImpl(ConfigRepo configRepo, AdminRepo adminRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, MenuRepo menuRepo, TokenRepo tokenRepo) {
        this.configRepo = configRepo;
        this.adminRepo = adminRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.menuRepo = menuRepo;
        this.tokenRepo = tokenRepo;
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
        EAdmin saved = this.adminRepo.save(admin);
        saved.setGrantedAdmins(null);
        saved.setParentAdmin(null);
        saved.getRole().setPrivileges(null);
        saved.getRole().setAdmins(null);
        return Result.success(saved);
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
    public Object createAdmin(AdminUserSubmit adminUserSubmit, EAdmin performer) {
        ERole role = this.roleRepo.findOne(adminUserSubmit.getRoleId());
        if (null == role) {
            return Result.fail(new ErrorR(ErrorR.SUBMIT_NEED_ROLE, SUBMIT_NEED_ROLE));
        }

        EAdmin admin = new EAdmin();
        BeanUtils.copyProperties(adminUserSubmit, admin);
        admin.setRole(role);
        admin.setParentAdmin(performer);
        EAdmin saved = this.adminRepo.save(admin);
        saved.setGrantedAdmins(null);
        saved.getParentAdmin().setGrantedAdmins(null);
        saved.getRole().setAdmins(null);
        saved.getRole().setPrivileges(null);
        return Result.success(saved);
    }

    @Override
    public Object changeAdminRole(EAdmin admin, EAdmin performer) {
        if (!this.checkPermission(admin, performer)) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_NOT_PERMITTED, ADMIN_NOT_PERMITTED));
        }
        return this.persistentNewAdmin(admin);
    }

    @Override
    public Object changeAdminPwd(EAdmin admin, EAdmin performer) {
        if (!this.checkPermission(admin, performer)) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_NOT_PERMITTED, ADMIN_NOT_PERMITTED));
        }
        return this.persistentNewAdmin(admin);
    }

    private boolean checkPermission(EAdmin admin, EAdmin performer) {
        EAdmin granter = admin.getParentAdmin();
        while (null != granter) {
            if (granter.getId().equals(performer.getId())) {
                return true;
            }
            granter = granter.getParentAdmin();
        }
        return false;
    }

    @Override
    public Object deleteAdmin(Long id) {
        try {
            this.adminRepo.delete(id);
        } catch (Exception ignored) {
            return Result.fail(Result.FAIL_ON_SQL);
        }
        return Result.success(Result.SUCCESS_MSG);
    }

    private Object persistentNewAdmin(EAdmin admin) {
        if (null == admin.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        EAdmin one = this.adminRepo.findOne(admin.getId());
        BeanUtils.copyProperties(admin, one);
        EAdmin saved = this.adminRepo.save(admin);
        saved.setGrantedAdmins(null);
        saved.getParentAdmin().setGrantedAdmins(null);
        saved.getRole().setPrivileges(null);
        saved.getRole().setAdmins(null);
        return Result.success(saved);
    }

    @Override
    public Object createRole(ERole role) {
        ERole saved = this.roleRepo.save(role);
        saved.setPrivileges(null);
        saved.setMenus(null);
        saved.setAdmins(null);
        return Result.success(saved);
    }

    @Override
    public Object changeRole(ERole role) {
        if (null == role.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        ERole one = this.roleRepo.findOne(role.getId());
        BeanUtils.copyProperties(role, one);
        ERole saved = this.roleRepo.save(role);
        saved.setPrivileges(null);
        saved.setMenus(null);
        saved.setAdmins(null);
        return Result.success(saved);
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
        EMenu saved = this.menuRepo.save(menu);
        saved.setRoles(null);
        return Result.success(saved);
    }

    @Override
    public Object changeMenu(EMenu menu) {
        if (null == menu.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, NO_ID_FOUND));
        }

        EMenu one = this.menuRepo.findOne(menu.getId());
        BeanUtils.copyProperties(menu, one);
        EMenu saved = this.menuRepo.save(menu);
        saved.setRoles(null);
        return Result.success(saved);
    }

    @Override
    public Object deleteMenu(Long id) {
        try {
            this.menuRepo.delete(id);
        } catch (Exception e) {
            return Result.fail(Result.FAIL_ON_SQL);
        }
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

    @Override
    public Object adminLogin(EAdmin admin) {
        if (null == admin.getName()) {
            return Result.fail(new ErrorR(ErrorR.SUBMIT_NEED_NAME, SUBMIT_NEED_NAME));
        }

        EAdmin one = this.adminRepo.findByNameEquals(admin.getName());
        if (null == one) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_NOT_FOUND, ADMIN_NOT_FOUND));
        }

        if (!one.getPwd().equals(admin.getPwd())) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_WRONG_PWD, ADMIN_WRONG_PWD));
        }

        EToken token = this.tokenRepo.findByAdmin_Id(one.getId());
        if (null == token) {
            token = new EToken();
            token.setAdmin(one);
        }
        Date now = new Date();
        token.setToken(StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(now)));
        token.setExpired(DateUtils.add(now, Calendar.DAY_OF_YEAR, 3));
        EToken saved = this.tokenRepo.save(token);
        one.setPwd(null);
        one.setGrantedAdmins(null);
        one.getRole().setAdmins(null);
        one.getRole().setPrivileges(null);

        if (one.getRole().isSuperRole()) {
            one.getRole().setMenus(this.menuRepo.findAll());
        }

        return Result.success(new AdminLoginView(one, saved));
    }

    @Override
    public Object listAdmins(EAdmin performer) {
        List<EAdmin> ret = new ArrayList<>();
        Queue<EAdmin> admins = new ArrayDeque<>(performer.getGrantedAdmins());

        while (!admins.isEmpty()) {
            EAdmin admin = admins.remove();
            ret.add(admin);
            admins.addAll(admin.getGrantedAdmins());
        }

        return Result.success(ret);
    }
}
