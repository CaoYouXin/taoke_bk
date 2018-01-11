package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EAdZoneItem;
import com.taoke.miquaner.data.ECate;
import com.taoke.miquaner.data.EHomeBtn;
import com.taoke.miquaner.repo.AdZoneItemRepo;
import com.taoke.miquaner.repo.CateRepo;
import com.taoke.miquaner.repo.HomeBtnRepo;
import com.taoke.miquaner.serv.IHomeServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeServImpl implements IHomeServ {

    private final HomeBtnRepo homeBtnRepo;
    private final CateRepo cateRepo;
    private final AdZoneItemRepo adZoneItemRepo;

    @Autowired
    public HomeServImpl(HomeBtnRepo homeBtnRepo, CateRepo cateRepo, AdZoneItemRepo adZoneItemRepo) {
        this.homeBtnRepo = homeBtnRepo;
        this.cateRepo = cateRepo;
        this.adZoneItemRepo = adZoneItemRepo;
    }

    @Override
    public Object getBanners() {
        return Result.success(this.homeBtnRepo.findAllByLocationTypeEqualsOrderByOrderDesc(EHomeBtn.BANNER));
    }

    @Override
    public Object postBanner(EHomeBtn banner) {
        return this.postHomeBtn(banner, EHomeBtn.BANNER);
    }

    @Override
    public Object getTools() {
        return Result.success(this.homeBtnRepo.findAllByLocationTypeEqualsOrderByOrderDesc(EHomeBtn.TOOL));
    }

    @Override
    public Object postTool(EHomeBtn tool) {
        return this.postHomeBtn(tool, EHomeBtn.TOOL);
    }

    @Override
    public Object getGroups() {
        return Result.success(this.homeBtnRepo.findAllByLocationTypeEqualsOrderByOrderDesc(EHomeBtn.GROUP));
    }

    @Override
    public Object postGroup(EHomeBtn group) {
        return this.postHomeBtn(group, EHomeBtn.GROUP);
    }

    @Override
    public Object deleteHomeBtn(Long id) {
        try {
            this.homeBtnRepo.delete(id);
        } catch (Exception ignored) {
            return Result.fail(Result.FAIL_ON_SQL);
        }
        return Result.success(Result.SUCCESS_MSG);
    }

    @Override
    public Object getCategories() {
        return Result.success(this.cateRepo.findAllByOrderByOrderDesc());
    }

    @Override
    public Object postCategory(ECate cate) {
        if (null != cate.getId()) {
            ECate one = this.cateRepo.findOne(cate.getId());
            if (null != one) {
                BeanUtils.copyProperties(cate, one);
                cate = one;
            }
        }
        ECate saved = this.cateRepo.save(cate);
        return Result.success(saved);
    }

    @Override
    public Object deleteCategory(Long id) {
        try {
            this.cateRepo.delete(id);
        } catch (Exception ignored) {
            return Result.fail(Result.FAIL_ON_SQL);
        }
        return Result.success(Result.SUCCESS_MSG);
    }

    @Override
    public Object getBtnList() {
        return Result.success(this.homeBtnRepo.findAll());
    }

    @Override
    public Object getAdZone(boolean isIos) {
        return isIos ? Result.success(this.adZoneItemRepo.findAllByOrderByIosOrderDesc())
                : Result.success(this.adZoneItemRepo.findAllByOrderByOrderDesc());
    }

    @Override
    public Object postAdZone(EAdZoneItem item) {
        if (null != item.getId()) {
            EAdZoneItem one = this.adZoneItemRepo.findOne(item.getId());
            BeanUtils.copyProperties(item, one);
            item = one;
        }

        EAdZoneItem saved = this.adZoneItemRepo.save(item);
        return Result.success(saved);
    }

    @Override
    public Object removeAdZone(Long id) {
        try {
            this.adZoneItemRepo.delete(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }
    }

    private Object postHomeBtn(EHomeBtn homeBtn, Integer locationType) {
        homeBtn.setLocationType(locationType);
        if (null != homeBtn.getId()) {
            EHomeBtn one = this.homeBtnRepo.findByIdEqualsAndLocationTypeEquals(homeBtn.getId(), locationType);
            if (null != one) {
                BeanUtils.copyProperties(homeBtn, one);
                homeBtn = one;
            }
        }
        EHomeBtn saved = this.homeBtnRepo.save(homeBtn);
        return Result.success(saved);
    }
}
