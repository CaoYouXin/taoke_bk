package com.taoke.miquaner.serv.impl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EPrivilege;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.fltr.AdminInterceptor;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.PrivilegeRepo;
import com.taoke.miquaner.repo.RoleRepo;
import com.taoke.miquaner.serv.IInitServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class InitServImpl implements IInitServ {

    @Autowired
    private ConfigRepo configRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PrivilegeRepo privilegeRepo;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Object init(ConfigurableApplicationContext context) {
        EConfig config = configRepo.findByKeyEquals(EConfig.SERVER_TOKEN);
        if (null == config) {
            config = new EConfig();
            config.setKey(EConfig.SERVER_TOKEN);
        }
        config.setValue(StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(new Date())));
        configRepo.save(config);

        ERole role = roleRepo.findByNameEquals(ERole.SUPER_ROLE_NAME);
        if (null == role) {
            role = new ERole();
            role.setName(ERole.SUPER_ROLE_NAME);
            roleRepo.save(role);
        } else {
            List<EAdmin> admins = role.getAdmins();
            if (admins.size() > 1) {
                System.err.println("ERROR: Multi Super Users Exist");
            }
        }

        RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        final List<EPrivilege> privileges = new ArrayList<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            RequestMapping requestMapping = handlerMethod.getMethod().getAnnotation(RequestMapping.class);
            if (null == requestMapping) {
                return;
            }

            String api = getApi(requestMapping);
            if (null == api) {
                return;
            }

            if (!api.startsWith("/admin/")) {
                return;
            }

            EPrivilege privilege = privilegeRepo.findByApiEquals(api);
            if (null == privilege) {
                privilege = new EPrivilege();
                privilege.setApi(api);
                privileges.add(privilege);
            }
        });
        this.privilegeRepo.save(privileges);

        return null;
    }

    private String getApi(RequestMapping requestMapping) {
        return requestMapping.value().length > 0 ? requestMapping.value()[0] : null;
    }

}
