package com.es.phoneshop.web.filter;

import com.es.phoneshop.service.DosProtectionService;
import com.es.phoneshop.service.impl.DefaultDosProtectionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;
    private static final int STATUS = 429;

    @Override
    public void init(FilterConfig filterConfig) {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (dosProtectionService.isAllowed(servletRequest.getRemoteAddr(), new Date())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(STATUS);
        }
    }
}
