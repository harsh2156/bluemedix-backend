package com.example.bluemedix_backend.aspect;

import com.example.bluemedix_backend.annotation.ApiRateLimited;
import com.example.bluemedix_backend.annotation.AuthRateLimited;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RateLimiter authRateLimiter;
    private final RateLimiter apiRateLimiter;

    @Around("@annotation(authRateLimited)")
    public Object authRateLimiterAround(ProceedingJoinPoint joinPoint, AuthRateLimited authRateLimited) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIp = getClientIp(request);
        
        log.debug("Auth rate limit check for IP: {}", clientIp);
        
        try {
            return authRateLimiter.executeCallable(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    if (throwable instanceof RuntimeException) {
                        throw (RuntimeException) throwable;
                    }
                    throw new RuntimeException(throwable);
                }
            });
        } catch (RequestNotPermitted e) {
            log.warn("Auth rate limit exceeded for IP: {}", clientIp);
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
            response.put("error", "Too Many Requests");
            response.put("message", "You have exceeded the rate limit. Please try again later.");
            
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }
    }

    @Around("@annotation(apiRateLimited)")
    public Object apiRateLimiterAround(ProceedingJoinPoint joinPoint, ApiRateLimited apiRateLimited) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIp = getClientIp(request);
        
        log.debug("API rate limit check for IP: {}", clientIp);
        
        try {
            return apiRateLimiter.executeCallable(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    if (throwable instanceof RuntimeException) {
                        throw (RuntimeException) throwable;
                    }
                    throw new RuntimeException(throwable);
                }
            });
        } catch (RequestNotPermitted e) {
            log.warn("API rate limit exceeded for IP: {}", clientIp);
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
            response.put("error", "Too Many Requests");
            response.put("message", "You have exceeded the rate limit. Please try again later.");
            
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}


