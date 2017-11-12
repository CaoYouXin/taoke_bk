package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EGuide;
import com.taoke.miquaner.data.EHelp;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.GuideRepo;
import com.taoke.miquaner.repo.HelpRepo;
import com.taoke.miquaner.serv.IAppServ;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServImpl implements IAppServ {

    private GuideRepo guideRepo;
    private HelpRepo helpRepo;
    private ConfigRepo configRepo;

    @Autowired
    public AppServImpl(GuideRepo guideRepo, HelpRepo helpRepo, ConfigRepo configRepo) {
        this.guideRepo = guideRepo;
        this.helpRepo = helpRepo;
        this.configRepo = configRepo;
    }

    @Override
    public Object listGuides() {
        return Result.success(this.guideRepo.findAll());
    }

    @Override
    public Object setGuide(EGuide guide) {
        if (null != guide.getId()) {
            EGuide one = this.guideRepo.findOne(guide.getId());
            if (null != one) {
                BeanUtils.copyProperties(guide, one);
                guide = one;
            }
        }

        EGuide saved = this.guideRepo.save(guide);
        return Result.success(saved);
    }

    @Override
    public Object removeGuide(Long id) {
        EGuide one = this.guideRepo.findOne(id);
        if (null == one) {
            return Result.success(null);
        }

        this.guideRepo.delete(id);
        return Result.success(null);
    }

    @Override
    public Object listHelp() {
        return Result.success(this.helpRepo.findAll());
    }

    @Override
    public Object setHelp(EHelp help) {
        if (null != help.getId()) {
            EHelp one = this.helpRepo.findOne(help.getId());
            if (null != one) {
                BeanUtils.copyProperties(help, one);
                help = one;
            }
        }

        EHelp saved = this.helpRepo.save(help);
        return Result.success(saved);
    }

    @Override
    public Object removeHelp(Long id) {
        EHelp one = this.helpRepo.findOne(id);
        if (null == one) {
            return Result.success(null);
        }

        this.guideRepo.delete(id);
        return Result.success(null);
    }

    @Override
    public Object setShareImgUrl(String shortUrl) {
        EConfig config = this.configRepo.findByKeyEquals(EConfig.APP_SHARE_IMG);
        if (null == config) {
            config = new EConfig();
            config.setValue(shortUrl);
        }

        EConfig saved = this.configRepo.save(config);
        return Result.success(saved);
    }

    @Override
    public Object getShareImgUrl() {
        EConfig config = this.configRepo.findByKeyEquals(EConfig.APP_SHARE_IMG);
        if (null == config) {
            return Result.success("");
        }
        return Result.success(config.getValue());
    }
}
