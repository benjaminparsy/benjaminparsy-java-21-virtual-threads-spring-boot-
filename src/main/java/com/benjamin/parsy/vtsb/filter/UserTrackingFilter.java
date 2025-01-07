package com.benjamin.parsy.vtsb.filter;

import com.benjamin.parsy.vtsb.data.ThreadSafeDataCollector;
import com.benjamin.parsy.vtsb.shared.web.HttpHeaderName;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestValueException;

import java.io.IOException;

/**
 * This filter records and logs calls made to the various endpoints
 */
@Component
@Order(1)
@Slf4j
public class UserTrackingFilter implements Filter {

    private final ThreadSafeDataCollector dataCollector;

    public UserTrackingFilter(ThreadSafeDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest httpServletRequest) {

            String userId = httpServletRequest.getHeader(HttpHeaderName.USER_ID.getName());

            boolean isDataAnalysis = httpServletRequest.getRequestURI().startsWith("/data");

            if (isDataAnalysis) {
                userId = "data-analyst";
            }

            if (!StringUtils.hasText(userId)) {
                throw new MissingRequestValueException("Required header 'userId' is not present.");
            }

            // Calls for data analysis are not recorded
            if (!isDataAnalysis) {
                dataCollector.recordUserCall(userId, httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
            }

            log.info("Call to {} {} by user '{}' on thread : {}",
                    httpServletRequest.getMethod(),
                    httpServletRequest.getRequestURI(),
                    userId,
                    Thread.currentThread());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
