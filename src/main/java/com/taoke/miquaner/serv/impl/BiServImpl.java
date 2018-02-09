package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EBiItemDetailClicked;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.BiItemDetailClickedRepo;
import com.taoke.miquaner.repo.UserRepo;
import com.taoke.miquaner.serv.IBiServ;
import com.taoke.miquaner.util.BiEUtil;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BiServImpl implements IBiServ {

    private final UserRepo userRepo;
    private final BiItemDetailClickedRepo biItemDetailClickedRepo;

    @Autowired
    public BiServImpl(UserRepo userRepo, BiItemDetailClickedRepo biItemDetailClickedRepo) {
        this.userRepo = userRepo;
        this.biItemDetailClickedRepo = biItemDetailClickedRepo;
    }

    @Override
    public Object itemDetailClicked(Long userId) {
        EUser user = this.userRepo.findOne(userId);
        if (null == user) {
            return Result.unAuth();
        }

        EBiItemDetailClicked bi = new EBiItemDetailClicked();
        bi.setUser(user);
        BiEUtil.set(bi, new Date());
        this.biItemDetailClickedRepo.save(bi);

        return Result.success(null);
    }
}
