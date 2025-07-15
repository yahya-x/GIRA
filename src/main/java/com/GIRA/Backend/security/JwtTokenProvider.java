package com.GIRA.Backend.security;

import com.GIRA.Backend.Entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT token provider for authentication and authorization.
 * <p>
 * Handles token generation, validation, and user extraction for various token types
 * including access tokens, refresh tokens, verification tokens, and password reset tokens.
 * Provides comprehensive security features with proper error handling and logging.
 * </p>
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 2025-07-14
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret:defaultSecretKeyForDevelopmentOnlyChangeInProduction}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:900}") // 15 minutes in seconds
    private int jwtExpirationInSeconds;

    @Value("${app.jwt.refresh-expiration:604800}") // 7 days in seconds
    private int jwtRefreshExpirationInSeconds;

    @Value("${app.jwt.issuer:gira-app}")
    private String jwtIssuer;

    /**
     * Generates a JWT access token for the authenticated user.
     * <p>
     * Creates a short-lived access token for API authentication with standard claims
     * including user ID, email, role, and token type.
     * </p>
     *
     * @param authentication The Spring Security authentication object
     * @return JWT access token string
     */
    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateToken(userPrincipal, jwtExpirationInSeconds * 1000L);
    }

    /**
     * Generates a JWT refresh token for the authenticated user.
     * <p>
     * Creates a long-lived refresh token for obtaining new access tokens without
     * requiring re-authentication.
     * </p>
     *
     * @param authentication The Spring Security authentication object
     * @return JWT refresh token string
     */
    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateToken(userPrincipal, jwtRefreshExpirationInSeconds * 1000L);
    }

    /**
     * Generates a JWT token directly from a User entity.
     * <p>
     * Convenience method for generating tokens when you have a User object
     * but not an Authentication object (e.g., in tests or background processes).
     * </p>
     *
     * @param user The User entity
     * @return JWT access token string
     */
    public String generateToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return generateToken(userPrincipal, jwtExpirationInSeconds * 1000L);
    }

    /**
     * Generates a JWT token with custom expiration.
     * <p>
     * Internal method that creates JWT tokens with specified expiration time
     * and standard claims including user information and token metadata.
     * </p>
     *
     * @param userPrincipal The UserPrincipal containing user information
     * @param expiration The token expiration time in milliseconds
     * @return JWT token string
     */
    private String generateToken(UserPrincipal userPrincipal, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPrincipal.getId().toString());
        claims.put("email", userPrincipal.getEmail());
        claims.put("role", userPrincipal.getRole());
        claims.put("type", "access");

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userPrincipal.getEmail())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .setIssuer(jwtIssuer)
                    .setId(UUID.randomUUID().toString())
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", userPrincipal.getEmail(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Generates a verification token for email verification.
     * <p>
     * Creates a token specifically for email verification with a 24-hour expiration
     * and appropriate claims for verification purposes.
     * </p>
     *
     * @param user The User entity requiring email verification
     * @return JWT verification token string
     */
    public String generateVerificationToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (24 * 60 * 60 * 1000L)); // 24 hours

        try {
            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .setIssuer(jwtIssuer)
                    .claim("type", "verification")
                    .claim("userId", user.getId().toString())
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating verification token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to generate verification token", e);
        }
    }

    /**
     * Generates a password reset token.
     * <p>
     * Creates a token specifically for password reset operations with a 1-hour expiration
     * and appropriate claims for password reset purposes.
     * </p>
     *
     * @param user The User entity requesting password reset
     * @return JWT password reset token string
     */
    public String generatePasswordResetToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (60 * 60 * 1000L)); // 1 hour

        try {
            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .setIssuer(jwtIssuer)
                    .claim("type", "password-reset")
                    .claim("userId", user.getId().toString())
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating password reset token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to generate password reset token", e);
        }
    }

    /**
     * Extracts the username (email) from the JWT token.
     *
     * @param token The JWT token string
     * @return The username/email from the token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token The JWT token string
     * @return The user ID as UUID
     */
    public UUID getUserIdFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return UUID.fromString(claims.get("userId", String.class));
        } catch (Exception e) {
            logger.error("Error extracting user ID from token", e);
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token string
     * @return The expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token The JWT token string
     * @param claimsResolver Function to extract the specific claim
     * @param <T> The type of the claim
     * @return The extracted claim value
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token string
     * @return All claims from the token
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token The JWT token string
     * @return true if the token is expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Validates the JWT token against user details.
     *
     * @param token The JWT token string
     * @param userDetails The user details to validate against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Validates the JWT token without requiring user details.
     * <p>
     * Performs basic validation including signature verification and expiration check.
     * </p>
     *
     * @param token The JWT token string
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Gets the signing key for JWT tokens.
     * <p>
     * Creates a secret key from the configured JWT secret for signing tokens.
     * If the secret is too weak for HS512, it will be padded or use a different approach.
     * </p>
     *
     * @return The secret key for JWT signing
     */
    private SecretKey getSigningKey() {
        try {
            // Try to create the key directly
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        } catch (Exception e) {
            logger.warn("Failed to create signing key from configured secret, using fallback: {}", e.getMessage());
            
            // Fallback: ensure the key is at least 64 bytes (512 bits) for HS512
            byte[] keyBytes = jwtSecret.getBytes();
            if (keyBytes.length < 64) {
                // Pad the key to 64 bytes
                byte[] paddedKey = new byte[64];
                System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 64));
                // Fill remaining bytes with the original key repeated
                for (int i = keyBytes.length; i < 64; i++) {
                    paddedKey[i] = keyBytes[i % keyBytes.length];
                }
                return Keys.hmacShaKeyFor(paddedKey);
            } else {
                // If key is already long enough, truncate to 64 bytes
                byte[] truncatedKey = new byte[64];
                System.arraycopy(keyBytes, 0, truncatedKey, 0, 64);
                return Keys.hmacShaKeyFor(truncatedKey);
            }
        }
    }

    /**
     * Extracts the token type from the JWT token.
     *
     * @param token The JWT token string
     * @return The token type (access, refresh, verification, password-reset)
     */
    public String getTokenType(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("type", String.class);
    }

    /**
     * Checks if the token is a verification token.
     *
     * @param token The JWT token string
     * @return true if it's a verification token, false otherwise
     */
    public boolean isVerificationToken(String token) {
        return "verification".equals(getTokenType(token));
    }

    /**
     * Checks if the token is a password reset token.
     *
     * @param token The JWT token string
     * @return true if it's a password reset token, false otherwise
     */
    public boolean isPasswordResetToken(String token) {
        return "password-reset".equals(getTokenType(token));
    }
} 