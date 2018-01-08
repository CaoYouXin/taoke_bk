package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EAdZoneItem;
import com.taoke.miquaner.data.ECate;
import com.taoke.miquaner.data.EHomeBtn;

public interface IHomeServ {

    Object getBanners();

    Object postBanner(EHomeBtn banner);

    Object getTools();

    Object postTool(EHomeBtn tool);

    Object getGroups();

    Object postGroup(EHomeBtn group);

    Object deleteHomeBtn(Long id);

    Object getCategories();

    Object postCategory(ECate cate);

    Object deleteCategory(Long id);

    Object getBtnList();

    Object getAdZone();

    Object postAdZone(EAdZoneItem item);

    Object removeAdZone(Long id);

}
