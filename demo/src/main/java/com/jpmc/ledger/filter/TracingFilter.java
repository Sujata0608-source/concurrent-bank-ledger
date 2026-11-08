package com.jpmc.ledger.filter;

import jakarta.servlet.Filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;

import org.springframework.stereotype.Component;
@Component
public class TracingFilter  implements Filter{

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            jakarta.servlet.FilterChain chain
    ) throws java.io.IOException, jakarta.servlet.ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Generate a unique ID for this request
        String correlationId = UUID.randomUUID().toString();

        // Store the ID in MDC
        MDC.put("correlationId", correlationId);
        httpResponse.setHeader("X-Correlation-ID", correlationId);

        // Request continues to the next filter or controller
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
        }
    }
}
