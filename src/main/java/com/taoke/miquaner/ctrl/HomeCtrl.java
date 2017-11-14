package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.ECate;
import com.taoke.miquaner.data.EHomeBtn;
import com.taoke.miquaner.serv.IHomeServ;
import com.taoke.miquaner.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/home/banner/list")
    public Object getBanners() {
        return this.homeServ.getBanners();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/banner/set", method = RequestMethod.POST)
    public Object setBanner(@RequestBody EHomeBtn banner) {
        return this.homeServ.postBanner(banner);
    }

    @RequestMapping("/home/tool/list")
    public Object getTools() {
        return this.homeServ.getTools();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/tool/set", method = RequestMethod.POST)
    public Object setTool(@RequestBody EHomeBtn tool) {
        return this.homeServ.postTool(tool);
    }

    @RequestMapping("/home/group/list")
    public Object getGroup() {
        return this.homeServ.getGroups();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/group/set", method = RequestMethod.POST)
    public Object setGroup(@RequestBody EHomeBtn group) {
        return this.homeServ.postGroup(group);
    }

    @RequestMapping("/home/cate/list")
    public Object getCategories() {
        return this.homeServ.getCategories();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/cate/set", method = RequestMethod.POST)
    public Object setCategory(@RequestBody ECate cate) {
        return this.homeServ.postCategory(cate);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/home/btn/list")
    public Object getBtnList() {
        return this.homeServ.getBtnList();
    }
}
