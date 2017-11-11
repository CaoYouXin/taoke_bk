package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EGuide;
import com.taoke.miquaner.data.EHelp;

public interface IAppServ {

    Object listGuides();

    Object setGuide(EGuide guide);

    Object removeGuide(Long id);

    Object listHelp();

    Object setHelp(EHelp help);

    Object removeHelp(Long id);

    Object setShareImgUrl(String shortUrl);

    Object getShareImgUrl();

}
