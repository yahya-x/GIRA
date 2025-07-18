package com.GIRA.Backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.GIRA.Backend.DTO.common.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT authentication entry point that handles unauthorized access attempts.
 * <p>
 * This component is responsible for handling authentication failures and returning
 * standardized error responses when users attempt to access protected resources
 * without proper authentication.
 * </p>
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 2025-07-14
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Handles authentication errors and returns a standardized API error response.
     * <p>
     * This method is called when an unauthenticated user attempts to access a protected resource.
     * It creates a standardized error response with appropriate HTTP status code and error details.
     * </p>
     *
     * @param request The HTTP request that resulted in an authentication exception
     * @param response The HTTP response to be sent to the client
     * @param authException The authentication exception that was thrown
     * @throws IOException If an I/O error occurs while writing the response
     * @throws ServletException If a servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                        AuthenticationException authException) throws IOException, ServletException {
        
        logger.warn("Unauthorized access attempt to: {} from IP: {}", 
                   request.getRequestURI(), getClientIpAddress(request));
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpServletResponse.SC_UNAUTHORIZED, 
            "Access denied. Authentication required.", 
            request.getRequestURI()
        );
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            logger.error("Error serializing authentication error response", e);
            // Fallback to simple error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }

    /**
     * Extracts the client IP address from the request.
     * <p>
     * Handles various proxy scenarios and returns the actual client IP address.
     * </p>
     *
     * @param request The HTTP request
     * @return The client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 