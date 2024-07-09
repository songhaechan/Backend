package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.jwt.domain.JwtSecretKey;
import com.amorgakco.backend.jwt.domain.RefreshToken;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.exception.IllegalAccessException;
import com.amorgakco.backend.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.jwt.exception.RefreshTokenRequiredException;
import com.amorgakco.backend.jwt.repository.RefreshTokenRepository;

import io.jsonwebtoken.Jwts;

import jakarta.servlet.http.Cookie;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
    private final JwtProperties jwtProperties;
    private final JwtValidator jwtValidator;
    private final JwtSecretKey jwtSecretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberJwt reissue(final String refreshToken, final String accessTokenHeader) {
        final RefreshToken savedRefreshToken = findRefreshTokenFromRedis(refreshToken);
        final String accessToken =
                extractAccessToken(accessTokenHeader).orElseThrow(InvalidJwtException::new);
        final String memberId = savedRefreshToken.getMemberId();
        if (jwtValidator.validateReissue(accessToken, memberId)) {
            refreshTokenRepository.delete(savedRefreshToken);
            return createAndSaveMemberToken(memberId);
        }
        throw new IllegalAccessException();
    }

    private RefreshToken findRefreshTokenFromRedis(final String token) {
        return refreshTokenRepository.findById(token).orElseThrow(InvalidJwtException::new);
    }

    public Optional<String> extractAccessToken(final String accessTokenWithBearer) {
        if (StringUtils.hasText(accessTokenWithBearer)
                && accessTokenWithBearer.startsWith(TOKEN_PREFIX)) {
            return Optional.of(accessTokenWithBearer.substring(TOKEN_PREFIX_LENGTH));
        }
        return Optional.empty();
    }

    public MemberJwt createAndSaveMemberToken(final String memberId) {
        final String accessToken = create(memberId, jwtProperties.accessExpiration());
        final String refreshToken = create(memberId, jwtProperties.refreshExpiration());
        refreshTokenRepository.save(new RefreshToken(refreshToken, memberId));
        return new MemberJwt(accessToken, refreshToken);
    }

    private String create(final String memberId, final Long duration) {
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + duration);
        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(jwtSecretKey.getSecretKey())
                .compact();
    }

    public void logout(final Optional<Cookie> cookie) {
        final Cookie tokenCookie = cookie.orElseThrow(RefreshTokenRequiredException::new);
        final String refreshToken = tokenCookie.getValue();
        refreshTokenRepository.deleteById(refreshToken);
    }
}
