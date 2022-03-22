package com.blue.optima.ratelimiter.strategy;

import io.github.bucket4j.Bucket;

import javax.servlet.http.HttpServletResponse;


public interface RateLimitStrategy {

    public boolean isRequestThrottledOrServed(String apiKey, HttpServletResponse response) throws Exception;

}
