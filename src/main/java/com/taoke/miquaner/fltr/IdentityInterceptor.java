package com.taoke.miquaner.fltr;

import com.taoke.miquaner.data.EToken;
import com.taoke.miquaner.repo.TokenRepo;
import com.taoke.miquaner.util.Auth;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);

            if (null == auth) {
                return true;
            }

            String authHeader = request.getHeader("auth");
            if (null == authHeader) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            EToken token = this.tokenRepo.findByTokenEqualsAndExpiredAfter(authHeader, new Date());

            if (null == token) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            if (auth.isAdmin()) {
                if (null != token.getAdmin()) {
                    request.setAttribute("admin", token.getAdmin());
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
//            } else {
//                if (null != token.getAdmin()) {
//                    request.setAttribute("admin", token.getAdmin());
//                } else {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return false;
//                }
            }

            return true;
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
