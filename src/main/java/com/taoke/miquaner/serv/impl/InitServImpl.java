package com.taoke.miquaner.serv.impl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.RoleRepo;
import com.taoke.miquaner.serv.IInitServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InitServImpl implements IInitServ {

    @Autowired
    private ConfigRepo configRepo;
    @Autowired
    private RoleRepo roleRepo;

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

        return null;
    }

}
