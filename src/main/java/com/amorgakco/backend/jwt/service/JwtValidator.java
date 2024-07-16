package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.global.exception.*;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private static final String EMPTY_CREDENTIAL = "";
    private final MemberRepository memberRepository;
    private final SecretKey secretKey;

    public Authentication getAuthentication(final String token) {
        checkAccessToken(token);
        final Member member =
                memberRepository
                        .findByIdWithRoles(Long.parseLong(getClaim(token)))
                        .orElseThrow(
                                () -> new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND));
        final MemberPrincipal memberPrincipal =
                new MemberPrincipal(getClaim(token), null, member.getRoleNames());
        return new UsernamePasswordAuthenticationToken(
                memberPrincipal, EMPTY_CREDENTIAL, getAuthorityList(member));
    }

    private void checkAccessToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new JwtAuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (final JwtException e) {
            throw new JwtAuthenticationException(ErrorCode.CANNOT_PARSE_TOKEN);
        }
    }

    public String getClaim(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private List<GrantedAuthority> getAuthorityList(final Member m) {
        return AuthorityUtils.createAuthorityList(
                m.getRoleNames().stream().map(r -> r.getRole().toString()).toList());
    }

    public boolean validateReissue(final String accessToken, final String refreshTokenMemberId) {
        try {
            checkAccessToken(accessToken);
        } catch (final TokenExpiredException e) {
            final String accessTokenMemberId = getClaim(accessToken);
            checkMemberId(refreshTokenMemberId, accessTokenMemberId);
            return true;
        }
        return false;
    }

    private static void checkMemberId(
            final String refreshTokenMemberId, final String accessTokenMemberId) {
        if (!accessTokenMemberId.equals(refreshTokenMemberId)) {
            throw new IllegalAccessException(ErrorCode.TOKEN_CLAIM_NOT_MATCHED);
        }
    }
}
