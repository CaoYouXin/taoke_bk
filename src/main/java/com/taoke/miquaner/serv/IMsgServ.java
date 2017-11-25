package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EUser;

public interface IMsgServ {

    Object listMessages(EUser user, Integer pageNo);

    Object getUncheckedMessageCount(EUser user);

    Object send2All(EAdmin admin, String title, String content);

    Object send2One(EAdmin admin, EUser user, String title, String content);

    Object send2One(EAdmin admin, Long userId, String title, String content);

    Object sendFeedback(EUser user, String content);

    Object readMessage(EUser user, Long mailBoxId);

    Object readFeedback();

    Object getSend2All(EAdmin admin);
}
