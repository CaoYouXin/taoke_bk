package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.view.AliMaMaSubmit;
import com.taoke.miquaner.view.ShareSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TbkCtrl {

    private ITbkServ tbkServ;

    @Autowired
    public TbkCtrl(ITbkServ tbkServ) {
        this.tbkServ = tbkServ;
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/setting", method = RequestMethod.GET)
    public Object getAliMaMa() {
        return this.tbkServ.getAliMaMa();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/setting", method = RequestMethod.POST)
    public Object setAliMaMa(@RequestBody AliMaMaSubmit aliMaMaSubmit) {
        return this.tbkServ.setAliMaMa(aliMaMaSubmit);
    }

    @Auth
    @RequestMapping(value = "/tbk/coupon/{cid}/{pNo}")
    public Object getCoupons(@PathVariable(name = "cid") String cid, @PathVariable(name = "pNo") Long pageNo, HttpServletRequest request) {
        return this.tbkServ.getCouponByCid(cid, pageNo,
                (EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/url/trans", method = RequestMethod.POST)
    public Object getShareLink(@RequestBody ShareSubmit shareSubmit) {
        return this.tbkServ.getShareLink(shareSubmit);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/fav/list/{pageNo}")
    public Object getFavoriteList(@PathVariable(name = "pageNo") Long pageNo) {
        return this.tbkServ.getFavoriteList(pageNo);
    }

    @Auth
    @RequestMapping("/tbk/fav/{favId}/list/{pageNo}")
    public Object getFavoriteItems(@PathVariable(name = "favId") Long favoriteId, @PathVariable(name = "pageNo") Long pageNo, HttpServletRequest request) {
        return this.tbkServ.getFavoriteItems(favoriteId, pageNo,
                (EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping("/tbk/search/{keyword}")
    public Object search(@PathVariable(name = "keyword") String keyword, HttpServletRequest request) {
        return this.tbkServ.search((EUser) request.getAttribute("user"), keyword, (Boolean) request.getAttribute("super"));
    }

    @RequestMapping("/tbk/hints/{keyword}")
    public Object hint(@PathVariable(name = "keyword") String keyword) {
        return this.tbkServ.hints(keyword);
    }

    @Auth
    @RequestMapping("/tbk/ju/{keyword}")
    public Object juSearch(@PathVariable(name = "keyword") String keyword, HttpServletRequest request) {
        return this.tbkServ.getJuItems((EUser) request.getAttribute("user"), keyword);
    }

    @RequestMapping("/tbk/hints/top")
    public Object hot() {
        return this.tbkServ.getTopSearchWords();
    }

}
