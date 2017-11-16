package com.taoke.miquaner.fltr;

import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EToken;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.TokenRepo;
import com.taoke.miquaner.util.Auth;
import com.taoke.miquaner.util.HttpUtils;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class IdentityInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(IdentityInterceptor.class);

    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private ConfigRepo configRepo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            logger.debug("handler is no HandlerMethod");
            return true;
        }

        Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);

        if (null == auth) {
            logger.debug("handler has no Auth annotation");
            return true;
        }

        String authHeader = request.getHeader("auth");
        if (null == authHeader) {
            HttpUtils.returnJSON(response, Result.unAuth());
            logger.debug("request has no auth header, returning 401");
            return false;
        }

        EToken token = this.tokenRepo.findByTokenEqualsAndExpiredAfter(authHeader, new Date());

        if (null == token) {
            HttpUtils.returnJSON(response, Result.unAuth());
            logger.debug("got no token by auth [" + authHeader + "], returning 401");
            return false;
        }

        if (auth.isAdmin()) {
            if (null != token.getAdmin()) {
                logger.info(String.format("Admin Id = %d", token.getAdmin().getId()));
                request.setAttribute("admin", token.getAdmin());
            } else {
                HttpUtils.returnJSON(response, Result.unAuth());
                logger.debug("token by auth [" + authHeader + "] associated with no admin, returning 401");
                return false;
            }
        } else {
            EUser user = token.getUser();
            if (null != user) {
                request.setAttribute("user", user);
                request.setAttribute("buyer", null == user.getAliPid());
                request.setAttribute("super", null == user.getpUser() && "platform_user".equals(user.getExt()));

                if (null == user.getAliPid() && null != user.getpUser()) {
                    user.setAliPid(user.getpUser().getAliPid());
                }

                if (null == user.getAliPid()) {
                    EConfig config = this.configRepo.findByKeyEquals(AliMaMaSubmit.PID_K);
                    if (null != config) {
                        user.setAliPid(config.getValue());
                    }
                }

                logger.info(String.format("User Id = %d", user.getId()));
            } else {
                HttpUtils.returnJSON(response, Result.unAuth());
                logger.debug("token by auth [" + authHeader + "] associated with no user, returning 401");
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
