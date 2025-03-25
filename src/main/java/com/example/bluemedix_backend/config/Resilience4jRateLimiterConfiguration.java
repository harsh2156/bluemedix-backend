// package com.example.bluemedix_backend.config;


// import io.github.resilience4j.ratelimiter.RateLimiter;
// import io.github.resilience4j.ratelimiter.RateLimiterConfig;
// import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.time.Duration;

// @Configuration
// public class RateLimiterConfig {

//     @Bean
//     public RateLimiterRegistry rateLimiterRegistry() {
//         return RateLimiterRegistry.ofDefaults();
//     }

//     @Bean
//     public RateLimiter authRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
//         RateLimiterConfig config = RateLimiterConfig.custom()
//                 .limitRefreshPeriod(Duration.ofSeconds(1))
//                 .limitForPeriod(20)
//                 .timeoutDuration(Duration.ZERO)
//                 .build();
        
//         return rateLimiterRegistry.rateLimiter("auth", config);
//     }

//     @Bean
//     public RateLimiter apiRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
//         RateLimiterConfig config = RateLimiterConfig.custom()
//                 .limitRefreshPeriod(Duration.ofSeconds(1))
//                 .limitForPeriod(100)
//                 .timeoutDuration(Duration.ZERO)
//                 .build();
        
//         return rateLimiterRegistry.rateLimiter("api", config);
//     }
// }


package com.example.bluemedix_backend.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class Resilience4jRateLimiterConfiguration {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.ofDefaults();
    }

    @Bean
    public RateLimiter authRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        RateLimiterConfig authConfig = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(20)
                .timeoutDuration(Duration.ZERO)
                .build();
        
        return rateLimiterRegistry.rateLimiter("auth", authConfig, Collections.emptyMap());
    }

    @Bean
    public RateLimiter apiRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        RateLimiterConfig apiConfig = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(100)
                .timeoutDuration(Duration.ZERO)
                .build();
        
        return rateLimiterRegistry.rateLimiter("api", apiConfig, Collections.emptyMap());
    }
}
