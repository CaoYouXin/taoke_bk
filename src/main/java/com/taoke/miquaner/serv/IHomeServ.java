package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EAdZoneItem;
import com.taoke.miquaner.data.ECate;
import com.taoke.miquaner.data.EFavoriteOrder;
import com.taoke.miquaner.data.EHomeBtn;

import java.util.List;

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

    Object getAdZone(boolean isIos);

    Object postAdZone(EAdZoneItem item);

    Object removeAdZone(Long id);

    Object postFavOrder(EFavoriteOrder favoriteOrder);

    Object removeFavOrder(Long favId, Long numIid);

    List<EFavoriteOrder> getFavOrder(Long favoriteId);

}
