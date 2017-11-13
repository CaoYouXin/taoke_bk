package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EGuide;
import com.taoke.miquaner.data.EHelp;
import com.taoke.miquaner.data.EShareImg;
import com.taoke.miquaner.serv.IAppServ;
import com.taoke.miquaner.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppCtrl {

    private IAppServ appServ;

    @Autowired
    public AppCtrl(IAppServ appServ) {
        this.appServ = appServ;
    }

    @RequestMapping("/app/guide/list")
    public Object getGuideList() {
        return this.appServ.listGuides();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/guide/set", method = RequestMethod.POST)
    public Object setGuide(@RequestBody EGuide guide) {
        return this.appServ.setGuide(guide);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/guide/remove/{id}")
    public Object removeGuide(@PathVariable(name = "id") Long id) {
        return this.appServ.removeGuide(id);
    }

    @RequestMapping("/app/help/list")
    public Object getHelpList() {
        return this.appServ.listHelp();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/help/set", method = RequestMethod.POST)
    public Object setHelp(@RequestBody EHelp help) {
        return this.appServ.setHelp(help);
    }

    @Auth(isAdmin = true)
    @RequestMapping("/app/help/remove/{id}")
    public Object removeHelp(@PathVariable(name = "id") Long id) {
        return this.appServ.removeHelp(id);
    }

    @RequestMapping(value = "/app/share/img/url/list", method = RequestMethod.GET)
    public Object getShareImgUrl() {
        return this.appServ.listShareImgUrl();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/share/img/url/set", method = RequestMethod.POST)
    public Object setShareImgUrl(@RequestBody EShareImg shareImg) {
        return this.appServ.setShareImgUrl(shareImg);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/share/img/url/remove/{id}")
    public Object setShareImgUrl(@PathVariable(name = "id") Long id) {
        return this.appServ.removeShareImgUrl(id);
    }

}
