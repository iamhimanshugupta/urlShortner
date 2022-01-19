package com.practice.urlshortener.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        request.setAttribute("UNIQUE_ID", new SecureRandom().nextInt(1000) + "" + System.currentTimeMillis());

        MDC.clear();
        MDC.put("UNIQUE_ID", (String) request.getAttribute("UNIQUE_ID"));

        filterChain.doFilter(request, response);
    }
}
