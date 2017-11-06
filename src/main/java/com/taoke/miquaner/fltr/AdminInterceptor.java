package com.taoke.miquaner.fltr;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.repo.AdminRepo;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.HttpUtils;
import com.taoke.miquaner.util.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(AdminInterceptor.class);

    @Autowired
    private AdminRepo adminRepo;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            logger.debug("handler is no HandlerMethod");
            return true;
        }

        Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);

        if (null == auth || !auth.isAdmin()) {
            logger.debug("handler has no Auth annotation or it request no admin permission");
            return true;
        }

        RequestMapping requestMapping = ((HandlerMethod) handler).getMethod().getAnnotation(RequestMapping.class);
        if (null == requestMapping) {
            logger.debug("handler has no RequestMapping annotation");
            return true;// actually impossible
        }

        EAdmin admin = (EAdmin) request.getAttribute("admin");
        if (null == admin) {
            HttpUtils.returnJSON(response, Result.unAuthAdmin());
            logger.debug("request has no Admin set, returning 401");
            return false;// actually impossible
        }

        admin = this.adminRepo.findOne(admin.getId());// gain lazy init feature
        ERole role = admin.getRole();
        boolean permitted = role.isSuperRole() ||
                role.getPrivileges().stream().anyMatch(ePrivilege -> ePrivilege.getApi().equals(requestMapping.value()[0]));
        if (!permitted) {
            HttpUtils.returnJSON(response, Result.unAuthAdmin());
            logger.debug("request is not permitted, returning 401");
        }

        return permitted;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }

}
