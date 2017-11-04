package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EBanner;
import com.taoke.miquaner.repo.BannerRepo;
import com.taoke.miquaner.serv.IHomeServ;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeServImpl implements IHomeServ {

    private BannerRepo bannerRepo;

    @Autowired
    public HomeServImpl(BannerRepo bannerRepo) {
        this.bannerRepo = bannerRepo;
    }

    @Override
    public Object getBanners() {
        return Result.success(this.bannerRepo.findAll());
    }

    @Override
    public Object postBanner(EBanner banner) {
        if (null != banner.getId()) {
            EBanner one = this.bannerRepo.findOne(banner.getId());
            BeanUtils.copyProperties(banner, one);
            banner = one;
        }
        this.bannerRepo.save(banner);
        return Result.success(banner);
    }
}
