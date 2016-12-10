package com.isc.project.manager.security.authentication;

import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    public static final String TYPE_FIELD = "type";

    @Autowired
    private UserEntityRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String tokenHeader = request.getHeader("Authentication");

        if(tokenHeader != null) {
            Jwt token = Jwts.parser().setSigningKey("secret").parse(tokenHeader);
            Claims claims = (Claims) token.getBody();

            if(claims.getExpiration().after(new Date())) {
                AuthenticatedUser user = new AuthenticatedUser();
                user.setId(((Number)claims.get(TokenField.USER_ID.name())).longValue());
                user.setUsername((String) claims.get(TokenField.USERNAME.name()));
                user.setType(UserType.valueOf((String) claims.get(TokenField.TYPE.name())));
                user.setTenantCode((String) claims.get(TokenField.TENANT_CODE.name()));

                SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(user, getUserTypeAuthorities(user.getType())));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private List<GrantedAuthority> getUserTypeAuthorities(UserType type) {
        switch (type) {
            case ADMIN:
                return Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            case MANAGER:
                return Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_MANAGER"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            default:
                return Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                );
        }
    }
}
