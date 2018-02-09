package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.serv.IBiServ;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BICtrl {

    private final IBiServ biServ;

    @Autowired
    public BICtrl(IBiServ biServ) {
        this.biServ = biServ;
    }

    @Auth
    @RequestMapping(value = "/bi/item/detail/clicked", method = RequestMethod.GET)
    public Object biItemDetailClicked(HttpServletRequest request) {
        EUser user = (EUser) request.getAttribute("user");
        if (null == user) {
            return Result.unAuth();
        }

        return this.biServ.itemDetailClicked(user.getId());
    }

}
