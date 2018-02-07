package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.view.AliMaMaSubmit;
import com.taoke.miquaner.view.ShareSubmit;
import com.taoke.miquaner.view.ShareSubmit2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TbkCtrl {

//    private final static Logger logger = LogManager.getLogger(TbkCtrl.class);
//
//    private final static Map<String, String> Domain2Selector;
//
//    static {
//        Map<String, String> map = new HashMap<>();
//        map.put("detail.m.tmall.com", "#modules-desc > div.module-container.item_picture > div > div > div");
//        map.put("h5.m.taobao.com", "#J_newDetail > div > div.d-InfoMain > div.d-tabBox > div.itemPhotoDetail > div > div");
//        Domain2Selector = Collections.unmodifiableMap(map);
//    }

    private final ITbkServ tbkServ;

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
    @RequestMapping(value = "/tbk/coupon/{cid}/{pNo}", method = RequestMethod.GET)
    public Object getCoupons(@PathVariable(name = "cid") String cid, @PathVariable(name = "pNo") Long pageNo, HttpServletRequest request) {
        return this.tbkServ.getCouponByCid(cid, pageNo,
                (EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/url/trans", method = RequestMethod.POST)
    public Object getShareLink(@RequestBody ShareSubmit shareSubmit) {
        return this.tbkServ.getShareLink(shareSubmit);
    }

    @Auth
    @RequestMapping(value = "/tbk/share/save", method = RequestMethod.POST)
    public Object shareSave(@RequestBody ShareSubmit2 shareSubmit2) {
        return this.tbkServ.getShareLink2(shareSubmit2);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/fav/list/{pageNo}", method = RequestMethod.GET)
    public Object getFavoriteList(@PathVariable(name = "pageNo") Long pageNo) {
        return this.tbkServ.getFavoriteList(pageNo);
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/fav/{favId}/list/all", method = RequestMethod.GET)
    public Object getFavoriteItems(@PathVariable(name = "favId") Long favoriteId) {
        return this.tbkServ.getFavoriteItems(favoriteId);
    }

    @Auth
    @RequestMapping(value = "/tbk/fav/{favId}/list/{pageNo}", method = RequestMethod.GET)
    public Object getFavoriteItems(@PathVariable(name = "favId") Long favoriteId, @PathVariable(name = "pageNo") Long pageNo, HttpServletRequest request) {
        return this.tbkServ.getFavoriteItems(favoriteId, pageNo,
                (EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/fav/{favId}/list/{pageNo}/v2", method = RequestMethod.GET)
    public Object getFavoriteItemsV2(@PathVariable(name = "favId") Long favoriteId, @PathVariable(name = "pageNo") Long pageNo, HttpServletRequest request) {
        return this.tbkServ.getFavoriteItemsV2(favoriteId, pageNo,
                (EUser) request.getAttribute("user"), (Boolean) request.getAttribute("super"));
    }

    @Auth
    @RequestMapping(value = "/tbk/search/{keyword}", method = RequestMethod.GET)
    public Object search(@PathVariable(name = "keyword") String keyword, HttpServletRequest request) {
        return this.tbkServ.search((EUser) request.getAttribute("user"), keyword, (Boolean) request.getAttribute("super"));
    }

    @RequestMapping(value = "/tbk/hints/{keyword}", method = RequestMethod.GET)
    public Object hint(@PathVariable(name = "keyword") String keyword) {
        return this.tbkServ.hints(keyword);
    }

    @Auth
    @RequestMapping(value = "/tbk/ju/{keyword}", method = RequestMethod.GET)
    public Object juSearch(@PathVariable(name = "keyword") String keyword, HttpServletRequest request) {
        return this.tbkServ.getJuItems((EUser) request.getAttribute("user"), keyword);
    }

    @RequestMapping(value = "/tbk/hints/top", method = RequestMethod.GET)
    public Object hot() {
        return this.tbkServ.getTopSearchWords();
    }

//    @RequestMapping(value = "/tbk/spider/{url:.+}", method = RequestMethod.GET)
//    public Object spider(@PathVariable(name = "url") String url) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(url).get();
//        } catch (IOException e) {
//            logger.error("", e);
//            return Result.failWithExp(e);
//        }
//
//        int beginIndex = url.indexOf("//") + 2;
//        int endIndex = url.indexOf('/', beginIndex);
//        Elements wrappers = doc.select(Domain2Selector.get(url.substring(beginIndex, endIndex)));
//        List<String> result = new ArrayList<>();
//        for (Element wrapper : wrappers) {
//            result.add(wrapper.child(0).attr("data-ks-lazyload"));
//        }
//
//        return Result.success(result);
//    }

}
