package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.view.AliMaMaSubmit;
import com.taoke.miquaner.view.ShareSubmit;

public interface ITbkServ {

    Object setAliMaMa(AliMaMaSubmit aliMaMaSubmit);

    Object getAliMaMa();

    Object getCouponByCid(String cid, Long pageNo, EUser user);

    Object getShareLink(ShareSubmit shareSubmit);

    Object getFavoriteList(Long pageNo);

    Object getFavoriteItems(Long favoriteId, Long pageNo, EUser user);

    Object search(EUser user, String keyword);

    Object hints(String keyword);

}
