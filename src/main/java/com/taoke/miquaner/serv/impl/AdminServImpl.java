package com.taoke.miquaner.serv.impl;

import com.mysql.jdbc.StringUtils;
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
    private static final String SUBMIT_NEED_NAME = "未指定用户名";
    private static final String ADMIN_NOT_FOUND = "没有该管理员";
    private static final String ADMIN_WRONG_PWD = "管理员密码错误，请联系您的上级管理员";
    private static final String ADMIN_NOT_PERMITTED = "该管理员并非您的权限范围";

    private final ConfigRepo configRepo;
    private final AdminRepo adminRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final MenuRepo menuRepo;
    private final TokenRepo tokenRepo;

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
        saved.setCreatedMessages(null);
        saved.getRole().setPrivileges(null);
        saved.getRole().setAdmins(null);
        return Result.success(saved);
    }

    @Override
    public Object getRoles() {
        List<ERole> all = this.roleRepo.findAll();
        return Result.success(
                all.stream().filter(eRole -> !eRole.isSuperRole())
                        .peek(this::roleToView)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Object createAdmin(AdminUserSubmit adminUserSubmit, EAdmin performer) {
        performer = this.adminRepo.findOne(performer.getId());

        ERole role = this.roleRepo.findOne(adminUserSubmit.getRoleId());
        if (null == role) {
            return Result.fail(new ErrorR(ErrorR.SUBMIT_NEED_ROLE, SUBMIT_NEED_ROLE));
        }

        EAdmin admin = new EAdmin();
        BeanUtils.copyProperties(adminUserSubmit, admin);
        admin.setRole(role);
        admin.setParentAdmin(performer);
        EAdmin saved = this.adminRepo.save(admin);
        return Result.success(adminToView(saved));
    }

    @Override
    public Object changeAdminRole(EAdmin admin, EAdmin performer) {
        EAdmin toPersist = this.checkPermission(admin, performer);
        if (null == toPersist) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_NOT_PERMITTED, ADMIN_NOT_PERMITTED));
        }
        return this.persistentNewAdmin(toPersist, admin);
    }

    @Override
    public Object changeAdminPwd(EAdmin admin, EAdmin performer) {
        EAdmin toPersist = this.checkPermission(admin, performer);
        if (null == toPersist) {
            return Result.fail(new ErrorR(ErrorR.ADMIN_NOT_PERMITTED, ADMIN_NOT_PERMITTED));
        }
        return this.persistentNewAdmin(toPersist, admin);
    }

    private EAdmin checkPermission(EAdmin admin, EAdmin performer) {
        admin = this.adminRepo.findOne(admin.getId());
        EAdmin granter = admin.getParentAdmin();
        while (null != granter) {
            if (granter.getId().equals(performer.getId())) {
                return admin;
            }
            granter = granter.getParentAdmin();
        }
        return null;
    }

    @Override
    public Object deleteAdmin(Long id) {
        try {
            EAdmin one = this.adminRepo.findOne(id);
            one.setDeleted(true);
            this.adminRepo.save(one);
        } catch (Exception ignored) {
            return Result.fail(Result.FAIL_ON_SQL);
        }
        return Result.success(Result.SUCCESS_MSG);
    }

    private Object persistentNewAdmin(EAdmin one, EAdmin admin) {
        if (null == admin.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        if (!StringUtils.isNullOrEmpty(admin.getPwd())) {
            one.setPwd(admin.getPwd());
        }

        if (null != admin.getRole()) {
            ERole role = this.roleRepo.findOne(admin.getRole().getId());
            if (null == role) {
                return Result.fail(new ErrorR(ErrorR.SUBMIT_NEED_ROLE, SUBMIT_NEED_ROLE));
            }
            one.setRole(role);
        }

        EAdmin saved = this.adminRepo.save(one);
        return Result.success(adminToView(saved));
    }

    @Override
    public Object createRole(ERole role) {
        ERole saved = this.roleRepo.save(role);
        roleToView(saved);
        return Result.success(saved);
    }

    private void roleToView(ERole saved) {
        saved.setPrivileges(saved.getPrivileges().stream().map(privilege -> {
            EPrivilege view = new EPrivilege();
            view.setApi(privilege.getApi());
            return view;
        }).collect(Collectors.toList()));
        saved.setMenus(saved.getMenus().stream().map(menu -> {
            EMenu view = new EMenu();
            view.setName(menu.getName());
            return view;
        }).collect(Collectors.toList()));
        saved.setAdmins(null);
    }

    @Override
    public Object changeRole(ERole role) {
        if (null == role.getId()) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        ERole one = this.roleRepo.findOne(role.getId());
        BeanUtils.copyProperties(role, one);
        ERole saved = this.roleRepo.save(role);
        roleToView(saved);
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
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
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
        if (null == one || (null != one.getDeleted() && one.getDeleted())) {
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

        EAdmin view = new EAdmin();
        BeanUtils.copyProperties(one, view, "parentAdmin", "grantedAdmins", "role", "createdMessages");

        ERole viewRole = new ERole();
        view.setRole(viewRole);
        if (one.getRole().isSuperRole()) {
            viewRole.setMenus(this.menuRepo.findAll());
        } else {
            viewRole.setMenus(one.getRole().getMenus());
        }

        viewRole.setMenus(viewRole.getMenus().stream().map(menu -> {
            EMenu viewMenu = new EMenu();
            BeanUtils.copyProperties(menu, viewMenu, "roles");
            return viewMenu;
        }).collect(Collectors.toList()));

        EToken viewToken = new EToken();
        BeanUtils.copyProperties(saved, viewToken, "admin", "user");

        return Result.success(new AdminLoginView(view, viewToken));
    }

    @Override
    public Object listAdmins(EAdmin performer) {
        // regain session
        performer = this.adminRepo.findOne(performer.getId());

        List<EAdmin> ret = new ArrayList<>();
        Queue<EAdmin> admins = new ArrayDeque<>(performer.getGrantedAdmins());

        while (!admins.isEmpty()) {
            EAdmin admin = admins.remove();
            EAdmin view = adminToView(admin);
            ret.add(view);
            admins.addAll(admin.getGrantedAdmins());
        }

        return Result.success(ret);
    }

    private EAdmin adminToView(EAdmin admin) {
        EAdmin view = new EAdmin();
        BeanUtils.copyProperties(admin, view, "parentAdmin", "grantedAdmins", "createdMessages");
        ERole role = view.getRole();
        ERole viewRole = new ERole();
        viewRole.setName(role.getName());
        view.setRole(viewRole);
        return view;
    }
}
