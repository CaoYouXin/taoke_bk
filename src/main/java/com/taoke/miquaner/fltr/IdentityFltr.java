package com.taoke.miquaner.fltr;

import javax.servlet.*;
import java.io.IOException;

public class IdentityFltr implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("test: before identity filter " + System.currentTimeMillis());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("test: after identity filter " + System.currentTimeMillis());
    }

    @Override
    public void destroy() {

    }
}
