package com.blue.optima.ratelimiter.strategy;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;


public class Context {

    RateLimitStrategy rateLimitStrategy;

    public Context(RateLimitStrategy rateLimitStrategy) {
        this.rateLimitStrategy = rateLimitStrategy;
    }

    public boolean performOperation(String key, HttpServletResponse response) throws Exception {
        return rateLimitStrategy.isRequestThrottledOrServed(key,response);
    }
}
