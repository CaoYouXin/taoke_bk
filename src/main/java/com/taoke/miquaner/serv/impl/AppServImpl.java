package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EGuide;
import com.taoke.miquaner.data.EHelp;
import com.taoke.miquaner.data.EShareImg;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.GuideRepo;
import com.taoke.miquaner.repo.HelpRepo;
import com.taoke.miquaner.repo.ShareImgRepo;
import com.taoke.miquaner.serv.IAppServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServImpl implements IAppServ {

    private final GuideRepo guideRepo;
    private final HelpRepo helpRepo;
    private final ShareImgRepo shareImgRepo;
    private final ConfigRepo configRepo;

    @Autowired
    public AppServImpl(ConfigRepo configRepo, GuideRepo guideRepo, HelpRepo helpRepo, ShareImgRepo shareImgRepo) {
        this.configRepo = configRepo;
        this.guideRepo = guideRepo;
        this.helpRepo = helpRepo;
        this.shareImgRepo = shareImgRepo;
    }

    @Override
    public Object listGuidesByType(Integer type) {
        return Result.success(this.guideRepo.findAllByTypeEqualsOrderByOrderDesc(type));
    }

    @Override
    public Object listGuides() {
        return Result.success(this.guideRepo.findAllByOrderByOrderDesc());
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

        this.helpRepo.delete(id);
        return Result.success(null);
    }

    @Override
    public Object listShareImgUrl() {
        return Result.success(this.shareImgRepo.findAllByOrderByOrderDesc());
    }

    @Override
    public Object listShareImgUrl(Integer type) {
        return Result.success(this.shareImgRepo.findAllByTypeEqualsOrderByOrderDesc(type));
    }

    @Override
    public Object setShareImgUrl(EShareImg shareImg) {
        if (null != shareImg.getId()) {
            EShareImg one = this.shareImgRepo.findOne(shareImg.getId());
            if (null != one) {
                BeanUtils.copyProperties(shareImg, one);
                shareImg = one;
            }
        }

        EShareImg saved = this.shareImgRepo.save(shareImg);
        return Result.success(saved);
    }

    @Override
    public Object removeShareImgUrl(Long id) {
        EShareImg one = this.shareImgRepo.findOne(id);
        if (null == one) {
            return Result.success(null);
        }

        this.shareImgRepo.delete(id);
        return Result.success(null);
    }

    @Override
    public Object getDownloadUrl(String key) {
        EConfig byKeyEquals = this.configRepo.findByKeyEquals(key);
        if (null == byKeyEquals) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        return Result.success(byKeyEquals.getValue());
    }
}
