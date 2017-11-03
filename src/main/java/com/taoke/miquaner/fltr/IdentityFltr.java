package com.taoke.miquaner.fltr;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class IdentityFltr implements Filter {

    private static final Logger logger = LogManager.getLogger(IdentityFltr.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("test: before identity filter");
        filterChain.doFilter(servletRequest, servletResponse);
        logger.info("test: after identity filter");
    }

    @Override
    public void destroy() {

    }
}
