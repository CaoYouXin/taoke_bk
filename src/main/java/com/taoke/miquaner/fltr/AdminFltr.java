package com.taoke.miquaner.fltr;

import javax.servlet.*;
import java.io.IOException;

public class AdminFltr implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("test: before admin filter " + System.currentTimeMillis());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("test: after admin filter " + System.currentTimeMillis());
    }

    @Override
    public void destroy() {

    }
}
