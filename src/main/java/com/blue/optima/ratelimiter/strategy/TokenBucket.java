package com.blue.optima.ratelimiter.strategy;

import com.blue.optima.ratelimiter.util.ThrottleRequestHelper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
Token Bucket Algorithm implementation
 */
public class TokenBucket implements RateLimitStrategy {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket getBucket(String apiKey) {
        Bandwidth requestLimit = ThrottleRequestHelper.getNumberOfRequestAllowed(apiKey);
        return Bucket4j.builder()
                .addLimit(requestLimit)
                .build();
    }


    @Override
    public boolean isRequestThrottledOrServed(String apiKey, HttpServletResponse response) throws Exception {

        Bucket tokenBucket = cache.computeIfAbsent(apiKey, this::getBucket);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");
            return false;
        }
    }

}
