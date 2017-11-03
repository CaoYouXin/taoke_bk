package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.repo.AdminRepo;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.RoleRepo;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminServImpl implements IAdminServ {

    private static final String ST_NOT_MATCH = "你的Token有误";
    private static final String NO_SUPER_ROLE = "没有超管权限可以绑定，错误可能发生在启动服务时";
    private static final String ALREADY_HAS_SUPER_USER = "系统里已经存在一个超级管理员了，超级管理员只能存在一个";

    @Autowired
    private ConfigRepo configRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private RoleRepo roleRepo;

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
        try {
            EAdmin saved = this.adminRepo.save(admin);
            if (null == saved.getId()) {
                return Result.fail(new ErrorR(ErrorR.CAN_NOT_SAVE_OBJECT, ErrorR.CAN_NOT_SAVE_OBJECT_MSG));
            }
            return Result.success(saved);
        } catch (Exception e) {
            return Result.fail(new ErrorR(ErrorR.CAN_NOT_SAVE_OBJECT, ErrorR.CAN_NOT_SAVE_OBJECT_MSG));
        }
    }

    @Override
    public Object getRoles() {
        return null;
    }


}
