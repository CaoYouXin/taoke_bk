package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EBanner;

public interface IHomeServ {

    Object getBanners();

    Object postBanner(EBanner banner);

}
