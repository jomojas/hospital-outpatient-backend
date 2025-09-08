package com.ncst.hospitaloutpatient.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @PostConstruct
    public void initKey() {
        // 推荐用这种方式将字符串密钥转为Key对象
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 判断token是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // 生成JWT
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)      // 自定义payload
                .setSubject(subject)    // 预留标准字段
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 校验和解析JWT
    public Claims parseAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    // 校验token有效性
    public boolean isTokenValid(String token) {
        Claims claims = parseAllClaims(token);
        return claims != null && !isTokenExpired(claims);
    }

    // 从token中获取用户名
    public String getUsername(String token) {
        Claims claims = parseAllClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    // 获取自定义字段
    public <T> T getClaim(String token, String key, Class<T> clazz) {
        Claims claims = parseAllClaims(token);
        if (claims != null) {
            Object value = claims.get(key);
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
        }
        return null;
    }

    // 刷新token
    public String refreshToken(String token) {
        Claims claims = parseAllClaims(token);
        if (claims == null) return null;
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + expirationTime));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}