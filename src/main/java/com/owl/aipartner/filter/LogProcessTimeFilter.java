package com.owl.aipartner.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@WebFilter(urlPatterns = "/*", filterName = "LogProcessTimeFilter")
public class LogProcessTimeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long processTime = System.currentTimeMillis() - startTime;
        log.info(processTime + " ms");
    }
}
