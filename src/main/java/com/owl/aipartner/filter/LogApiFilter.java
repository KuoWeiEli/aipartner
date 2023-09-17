package com.owl.aipartner.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogApiFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String requestInfo = getRequestInfo(request);
        log.info("Receive request: " + requestInfo
                + " , and request: " + getContent(requestWrapper.getContentAsByteArray()));

        chain.doFilter(requestWrapper, responseWrapper);

        log.info("Send " + response.getStatus() + " response from: " + requestInfo +
                ", and response is " + getContent(responseWrapper.getContentAsByteArray()));
        responseWrapper.copyBodyToResponse();
    }

    private String getRequestInfo(HttpServletRequest request) {
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();

        if (params != null) {
            uri += "?" + params;
        }

        return String.join(" ", httpMethod, uri);
    }

    private String getContent(byte[] content) {
        String body = new String(content);
        return body.replaceAll("[\n\t]", "");
    }
}
