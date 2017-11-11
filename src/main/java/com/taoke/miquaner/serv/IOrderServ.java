package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;

import java.io.IOException;
import java.util.List;

public interface IOrderServ {

    Object upload(String filePath) throws IOException;

    Object list(EUser user, Integer type, Integer pageNo);

    Object getChildUserCommit(List<EUser> children);

    Object withdraw(EUser user, Double amount);

    Object canDraw(EUser user);

    Object lastMonthSettled(EUser user);

    Object thisMonthSettled(EUser user);

}
