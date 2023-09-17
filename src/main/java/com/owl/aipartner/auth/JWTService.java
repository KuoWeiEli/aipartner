package com.owl.aipartner.auth;

import java.security.Key;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    @Autowired
    private AuthenticationProvider authProvider;

    private final String KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public String generateToken(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());
        authentication = authProvider.authenticate(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        claims.setExpiration(calendar.getTime());
        claims.setIssuer("Owl Flyfly");

        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> parseToken(String token) {
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}