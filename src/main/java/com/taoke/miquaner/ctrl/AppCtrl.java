package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EGuide;
import com.taoke.miquaner.data.EHelp;
import com.taoke.miquaner.data.EShareImg;
import com.taoke.miquaner.serv.IAppServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AppCtrl {

    private IAppServ appServ;

    @Autowired
    public AppCtrl(IAppServ appServ) {
        this.appServ = appServ;
    }

    @RequestMapping(value = "/app/guide/list/{type}", method = RequestMethod.GET)
    public Object getGuidesByType(@PathVariable(name = "type") Integer type) {
        return this.appServ.listGuidesByType(type);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/guide/list", method = RequestMethod.GET)
    public Object getGuideList() {
        return this.appServ.listGuides();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/guide/set", method = RequestMethod.POST)
    public Object setGuide(@RequestBody EGuide guide) {
        return this.appServ.setGuide(guide);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/guide/remove/{id}", method = RequestMethod.GET)
    public Object removeGuide(@PathVariable(name = "id") Long id) {
        return this.appServ.removeGuide(id);
    }

    @RequestMapping(value = "/app/help/list", method = RequestMethod.GET)
    public Object getHelpList() {
        return this.appServ.listHelp();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/help/set", method = RequestMethod.POST)
    public Object setHelp(@RequestBody EHelp help) {
        return this.appServ.setHelp(help);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/help/remove/{id}", method = RequestMethod.GET)
    public Object removeHelp(@PathVariable(name = "id") Long id) {
        return this.appServ.removeHelp(id);
    }

    @RequestMapping(value = "/app/share/img/url/list/{type}", method = RequestMethod.GET)
    public Object getShareImgUrl(@PathVariable(name = "type") Integer type) {
        return this.appServ.listShareImgUrl(type);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/share/img/url/list/all", method = RequestMethod.GET)
    public Object getShareImgUrl() {
        return this.appServ.listShareImgUrl();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/share/img/url/set", method = RequestMethod.POST)
    public Object setShareImgUrl(@RequestBody EShareImg shareImg) {
        return this.appServ.setShareImgUrl(shareImg);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/app/share/img/url/remove/{id}", method = RequestMethod.GET)
    public Object setShareImgUrl(@PathVariable(name = "id") Long id) {
        return this.appServ.removeShareImgUrl(id);
    }

    @RequestMapping(value = "/app/download/url", method = RequestMethod.GET)
    public Object getDownloadUrl(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        if (null == platform) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        switch (platform) {
            case "android":
                return this.appServ.getDownloadUrl(AliMaMaSubmit.ANDROID_URL);
            case "ios":
                return this.appServ.getDownloadUrl(AliMaMaSubmit.IOS_URL);
            default:
                return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }
    }

}
