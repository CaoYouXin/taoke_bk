package com.taoke.miquaner.fltr;

import com.taoke.miquaner.data.EPrivilege;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.PrivilegeRepo;
import com.taoke.miquaner.util.HttpUtils;
import com.taoke.miquaner.util.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(VersionInterceptor.class);

    @Autowired
    private PrivilegeRepo privilegeRepo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            logger.debug("handler is no HandlerMethod");
            return true;
        }

        RequestMapping requestMapping = ((HandlerMethod) handler).getMethod().getDeclaredAnnotation(RequestMapping.class);
        if (null == requestMapping) {
            logger.debug("handler has no RequestMapping annotation");
            return true;
        }

        if (requestMapping.value().length < 1) {
            logger.debug("RequestMapping has no value");
            return true;
        }

        if (requestMapping.method().length < 1) {
            logger.debug("RequestMapping has no method");
            return true;
        }

        String platform = request.getHeader("platform");
        if (null == platform) {
            logger.debug("api platform not supported");
            return true;
        }

        String version = request.getHeader("version");
        if (null == version) {
            logger.debug("api version not supported");
            return true;
        }

        logger.debug(String.format("client info : %s, %s. request %s", platform, version, requestMapping.value()[0]));

        EPrivilege privilege = privilegeRepo.findByApiEqualsAndMethodEquals(requestMapping.value()[0], requestMapping.method()[0].name());
        if (null == privilege) {
            logger.debug("api not found");
            return true;
        }

        String requestVersion;
        switch (platform) {
            case "android":
                requestVersion = privilege.getAndroidVersion();
                break;
            case "ios":
                requestVersion = privilege.getiOSVersion();
                break;
            case "web":
                requestVersion = privilege.getWebVersion();
                break;
            default:
                requestVersion = null;
        }

        if (checkVersion(version, requestVersion)) {
            logger.info("client version correct.");
            return true;
        }

        HttpUtils.returnJSON(response, Result.versionLow());
        logger.info("client version low.");
        return false;
    }

    private boolean checkVersion(String version, String requestVersion) {
        if (null == requestVersion) {
            return true;
        }

        String[] versions = version.split("\\.");
        String[] requestVersions = requestVersion.split("\\.");
        int i = 0;
        for (; i < versions.length && i < requestVersions.length; i++) {
            try {
                int v = Integer.parseInt(versions[i]);
                int rv = Integer.parseInt(requestVersions[i]);

                if (v < rv) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return i == requestVersions.length;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
