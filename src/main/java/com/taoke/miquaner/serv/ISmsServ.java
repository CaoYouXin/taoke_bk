package com.taoke.miquaner.serv;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface ISmsServ {

    boolean send(String templateCode, String phone, Map<String, String> params) throws JsonProcessingException;

}
