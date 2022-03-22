package com.blue.optima.ratelimiter.interceptor;

import com.blue.optima.ratelimiter.strategy.RateLimitAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
The Interceptor intercepts the rate limit before actual serving the request.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitAssigner rateLimitAssigner;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getHeader("user-id");
        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: user-id");
            return false;
        }

        return rateLimitAssigner.assignTokenWithRateLimitCapacity(apiKey + request.getRequestURI(), response);
    }
}
