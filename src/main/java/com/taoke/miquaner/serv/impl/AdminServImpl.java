package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.serv.IAdminServ;
import com.taoke.miquaner.view.SuperUserSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServImpl implements IAdminServ {

    @Autowired
    private ConfigRepo configRepo;

    @Override
    public Object setSuperUser(SuperUserSubmit superUserSubmit) {
        return null;
    }
}
