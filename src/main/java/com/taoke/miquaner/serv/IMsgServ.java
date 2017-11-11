package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;

public interface IMsgServ {

    Object listMessages(EUser user, Integer pageNo);

    Object getUncheckedMessageCount(EUser user);

    Object send2All(String title, String content);

    Object sendFeedback(EUser user, String content);

}
