package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EAdZoneItem;
import com.taoke.miquaner.data.ECate;
import com.taoke.miquaner.data.EHomeBtn;
import com.taoke.miquaner.serv.IHomeServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeCtrl {

    private final IHomeServ homeServ;

    @Autowired
    public HomeCtrl(IHomeServ homeServ) {
        this.homeServ = homeServ;
    }

    @RequestMapping(value = "/home/banner/list", method = RequestMethod.GET)
    public Object getBanners() {
        return this.homeServ.getBanners();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/banner/set", method = RequestMethod.POST)
    public Object setBanner(@RequestBody EHomeBtn banner) {
        return this.homeServ.postBanner(banner);
    }

    @RequestMapping(value = "/home/tool/list", method = RequestMethod.GET)
    public Object getTools() {
        return this.homeServ.getTools();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/tool/set", method = RequestMethod.POST)
    public Object setTool(@RequestBody EHomeBtn tool) {
        return this.homeServ.postTool(tool);
    }

    @RequestMapping(value = "/home/group/list", method = RequestMethod.GET)
    public Object getGroup() {
        return this.homeServ.getGroups();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/group/set", method = RequestMethod.POST)
    public Object setGroup(@RequestBody EHomeBtn group) {
        return this.homeServ.postGroup(group);
    }

    @RequestMapping(value = "/home/cate/list", method = RequestMethod.GET)
    public Object getCategories() {
        return this.homeServ.getCategories();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/cate/set", method = RequestMethod.POST)
    public Object setCategory(@RequestBody ECate cate) {
        return this.homeServ.postCategory(cate);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/cate/del/{id}", method = RequestMethod.GET)
    public Object removeCategory(@PathVariable(name = "id") Long id) {
        return this.homeServ.deleteCategory(id);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/btn/list", method = RequestMethod.GET)
    public Object getBtnList() {
        return this.homeServ.getBtnList();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/btn/del/{id}", method = RequestMethod.GET)
    public Object removeHomeBtn(@PathVariable(name = "id") Long id) {
        return this.homeServ.deleteHomeBtn(id);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/adZone/set", method = RequestMethod.POST)
    public Object setAdZone(@RequestBody EAdZoneItem item) {
        return this.homeServ.postAdZone(item);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/home/adZone/del/{id}", method = RequestMethod.GET)
    public Object removeAdZone(@PathVariable(name = "id") Long id) {
        return this.homeServ.removeAdZone(id);
    }

    @RequestMapping(value = "/home/adZone/list", method = RequestMethod.GET)
    public Object getAdZone(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        if (null == platform) {
            platform = "";
        }
        switch (platform) {
            case "ios":
                return this.homeServ.getAdZone(true);
            case "android":
            case "web":
            default:
                return this.homeServ.getAdZone(false);
        }
    }

}
