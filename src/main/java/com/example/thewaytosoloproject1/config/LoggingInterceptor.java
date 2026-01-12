package com.example.thewaytosoloproject1.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute("startTime", start);

        log.info(">>> [{}] {} - handler: {}",
                request.getMethod(),
                request.getRequestURI(),
                handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        log.info("<<< postHandle [{}] {}", request.getMethod(), request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        long duration = startTime == null ? -1 : (System.currentTimeMillis() - startTime);

        log.info("<<< afterCompletion [{}] {} - status: {}, time: {} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration
        );

        if (ex != null) {
            log.error("Exception during request processing", ex);
        }
    }
}
