package com.hotelvista.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Set<String> ADMIN_PERMISSIONS = Set.of(
            "booking_view", "booking_create", "booking_edit", "booking_delete",
            "booking_cancel", "booking_checkin", "booking_checkout", "booking_manage",
            "customer_view", "customer_create", "customer_edit", "customer_delete",
            "employee_view", "employee_create", "employee_edit", "employee_delete",
            "room_view", "room_manage", "room_type_manage",
            "service_view", "service_manage",
            "news_view", "news_manage",
            "promotion_view", "promotion_manage", "promotion_type_manage",
            "voucher_view", "voucher_manage",
            "review_manage", "pricing_manage",
            "report_view", "report_create", "report_delete", "analytics_view",
            "ai_chat"
    );

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractBearerToken(request);
        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String userId = claims.getSubject();
                Set<String> roles = readStringSet(claims.get("roles"));
                Set<String> permissions = readStringSet(claims.get("permissions"));
                setAuthentication(userId, roles, permissions);
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private Set<String> readStringSet(Object value) {
        if (value instanceof Collection<?> values) {
            return values.stream().map(String::valueOf).collect(Collectors.toSet());
        }
        return Set.of();
    }

    private void setAuthentication(String principal, Set<String> roles, Set<String> permissions) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(normalizeRoleAuthority(role))));
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        if (hasAdminRole(roles)) {
            ADMIN_PERMISSIONS.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean hasAdminRole(Set<String> roles) {
        return roles.stream()
                .map(role -> role.trim().toUpperCase())
                .anyMatch(role -> role.equals("ADMIN") || role.equals("ROLE_ADMIN"));
    }

    private String normalizeRoleAuthority(String role) {
        String normalizedRole = role.trim().toUpperCase();
        return normalizedRole.startsWith("ROLE_") ? normalizedRole : "ROLE_" + normalizedRole;
    }
}
