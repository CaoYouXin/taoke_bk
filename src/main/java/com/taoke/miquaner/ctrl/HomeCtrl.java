package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EBanner;
import com.taoke.miquaner.serv.IHomeServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeCtrl {

    private IHomeServ homeServ;

    @Autowired
    public HomeCtrl(IHomeServ homeServ) {
        this.homeServ = homeServ;
    }

    @RequestMapping("/admin/banner/list")
    public Object getBanners() {
        return this.homeServ.getBanners();
    }

    @RequestMapping(value = "/admin/banner/set", method = RequestMethod.POST)
    public Object setBanner(EBanner banner) {
        return this.homeServ.postBanner(banner);
    }

}
