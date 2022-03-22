package com.blue.optima.ratelimiter.util;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the rate limitation capacity for all users. It acts as in-memory storage.
 */
@Component
public final class ThrottleRequestHelper {

    static Map<String, Bandwidth> map = new HashMap<>();


    @PostConstruct
    public void intializeThrottleRequestData() {
        map.put(AppConstant.API_V_1_DEVELOPERS, Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(2))));
        map.put(AppConstant.API_V_1_DEVELOPERS_3, Bandwidth.classic(3, Refill.intervally(3, Duration.ofSeconds(1))));
        map.put(AppConstant.API_V_1_ORGANIZATIONS_4, Bandwidth.classic(4, Refill.intervally(4, Duration.ofSeconds(1))));
        map.put(AppConstant.API_V_1_ORGANIZATIONS_2, Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1))));
    }

    /*
    If user is registered then specific value is return otherwise default(i.e 1)
       rate limit capacity is returned
     */
    public static Bandwidth getNumberOfRequestAllowed(String key) {
        return map.getOrDefault(key, Bandwidth.classic(1, Refill.intervally(1, Duration.ofHours(1))));
    }
}
