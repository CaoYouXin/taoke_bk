package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;

import java.io.IOException;

public interface IOrderServ {

    Object upload(String filePath) throws IOException;

    Object list(EUser user, Integer type, Integer pageNo);

}
