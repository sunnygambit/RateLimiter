package com.blue.optima.ratelimiter.integration.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(GenericController.class)
//@DisplayName("Spring boot 2 mockito2 Junit5 example")
public class GenericControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void developerTest() throws Exception {
        String url = "/api/v1/developers";
        IntStream.rangeClosed(1, 2)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(counter -> {
                    successfulWebRequest(url, counter - 1);
                });

        blockedWebRequestDueToRateLimit(url,"1");
    }


     @Test
    public void organizationsTest() throws Exception {
        String url = "/api/v1/organizations";
        IntStream.rangeClosed(1, 4)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(counter -> {
                    successfulWebRequest(url, counter - 1);
                });

        blockedWebRequestDueToRateLimit(url,"1");
    }

    private void successfulWebRequest(String url, Integer remainingTries) {
        try {
            List<String> actual = Arrays.asList("sunny", "Amit");
            this.mockMvc
                    .perform(get(url)
                            .header("user-id", "1")
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().longValue("X-Rate-Limit-Remaining", remainingTries))
                    .andExpect((ResultMatcher) jsonPath("$", hasSize(2)));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void blockedWebRequestDueToRateLimit(String url,String userId) throws Exception {
        this.mockMvc
                .perform(get(url).header("user-id", userId))
                .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()));

    }

}
