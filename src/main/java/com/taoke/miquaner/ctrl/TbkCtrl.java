package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TbkCtrl {

    private ITbkServ tbkServ;

    @Autowired
    public TbkCtrl(ITbkServ tbkServ) {
        this.tbkServ = tbkServ;
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/setting", method = RequestMethod.GET)
    public Object getAliMaMa() {
        return this.tbkServ.getAliMaMa();
    }

    @Auth(isAdmin = true)
    @RequestMapping(value = "/tbk/setting", method = RequestMethod.POST)
    public Object setAliMaMa(@RequestBody AliMaMaSubmit aliMaMaSubmit) {
        return this.tbkServ.setAliMaMa(aliMaMaSubmit);
    }

}
