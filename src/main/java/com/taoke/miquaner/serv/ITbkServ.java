package com.taoke.miquaner.serv;

import com.taoke.miquaner.view.AliMaMaSubmit;

public interface ITbkServ {

    Object setAliMaMa(AliMaMaSubmit aliMaMaSubmit);

    Object getAliMaMa();

    Object getCouponByCid(String cid, Long pageNo, Long adZoneId);

}
