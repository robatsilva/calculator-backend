package com.ntd.calculator.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Key for signing the JWT
    private final long expirationTime = 86400000; // 1 day in milliseconds

    /**
     * Generates a JWT token for the given username.
     * 
     * @param username the username to include in the JWT
     * @return a JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Sets the subject of the token to the username
                .setIssuedAt(new Date()) // Sets the issue time of the token
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Sets the expiration time
                .signWith(key) // Signs the token with the secret key
                .compact();
    }

    /**
     * Validates a JWT token and retrieves the username (subject) from it.
     * 
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates the token and checks if it is expired.
     * 
     * @param token the JWT token
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // Extract claims to validate
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // If there's any exception during parsing, token is invalid
        }
    }

    /**
     * Extracts claims from the token using a function.
     * 
     * @param <T> the type of the claim to extract
     * @param token the JWT token
     * @param claimsResolver a function to extract a specific claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     * 
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a token is expired.
     * 
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
