package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EPrivilege;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.repo.AdminRepo;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.PrivilegeRepo;
import com.taoke.miquaner.repo.RoleRepo;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.JpaUtil;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AdminUserSubmit;
import com.taoke.miquaner.view.BindSubmit;
import com.taoke.miquaner.view.RoleSubmit;
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

    @Autowired
    private ConfigRepo configRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PrivilegeRepo privilegeRepo;

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

        return JpaUtil.persistent(this.adminRepo, admin);
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

        return JpaUtil.persistent(this.adminRepo, admin);
    }

    @Override
    public Object createRole(RoleSubmit roleSubmit) {
        ERole role = new ERole();
        BeanUtils.copyProperties(roleSubmit, role);

        return JpaUtil.persistent(this.roleRepo, role);
    }

    @Override
    public Object getPrivileges() {
        return Result.success(this.privilegeRepo.findAll().stream().peek(ePrivilege -> {
            ePrivilege.setRoles(null);
        }).collect(Collectors.toList()));
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

}
