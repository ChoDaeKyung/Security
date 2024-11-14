package com.example.tobi.tokenproject.config.filter;


import com.example.tobi.tokenproject.config.jwt.TokenProvider;
import com.example.tobi.tokenproject.model.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        // 검증 로직
        String requestURI = request.getRequestURI();
        log.info("requestURI: {}", requestURI);

        String token = resolveToken(request);

        int validateToken =  tokenProvider.validateToken(token);
        System.out.println("문제번호 : " + validateToken);
        if (token != null && validateToken == 1) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = tokenProvider.getTokenDetails(token);
            request.setAttribute("member", member);

        }else if (token != null && validateToken == 2) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}