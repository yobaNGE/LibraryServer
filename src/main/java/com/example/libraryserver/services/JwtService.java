package com.example.libraryserver.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private static final String SECRET_KEY = ("9c56bbb2442aa20f7d48ce5ba13b75c38000266334fb008387322a8a8ff24944").toUpperCase();

    /**
     * Извлекает имя пользователя из JWT токена.
     *
     * @param jwtToken JWT токен
     * @return имя пользователя
     */
    public String extractUsername(String jwtToken) {
        return ExtractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * Извлекает определенное утверждение из JWT токена.
     *
     * @param jwtToken JWT токен
     * @param claimsResolver функция для извлечения утверждения
     * @return утверждение
     */
    public <T> T ExtractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * Генерирует JWT токен для пользователя.
     *
     * @param userDetails детали пользователя
     * @return JWT токен
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Генерирует JWT токен с утверждениями для пользователя.
     *
     * @param claims утверждения
     * @param userDetails детали пользователя
     * @return JWT токен
     */
    public String generateToken(Map<String, Objects> claims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60L * 60L * 24L * 30L)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Проверяет валидность JWT токена для пользователя.
     *
     * @param jwtToken JWT токен
     * @param userDetails детали пользователя
     * @return true, если токен валиден, иначе false
     */
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    /**
     * Проверяет, истекло ли время действия JWT токена.
     *
     * @param jwtToken JWT токен
     * @return true, если время действия истекло, иначе false
     */
    private boolean isTokenExpired(String jwtToken) {
        return extractExpirationTime(jwtToken).before(new Date());
    }

    /**
     * Извлекает время истечения JWT токена.
     *
     * @param jwtToken JWT токен
     * @return время истечения
     */
    private Date extractExpirationTime(String jwtToken) {
        return ExtractClaim(jwtToken, Claims::getExpiration);
    }

    /**
     * Извлекает все утверждения из JWT токена.
     *
     * @param jwtToken JWT токен
     * @return утверждения
     */
    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * Возвращает ключ для подписи JWT токена.
     *
     * @return ключ
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
