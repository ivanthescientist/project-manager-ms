package com.isc.project.manager.controller;

import com.isc.project.manager.persistence.domain.UserEntity;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import com.isc.project.manager.security.TokenField;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class AuthenticationController {
    @Autowired
    private UserEntityRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/api/authenticate")
    public String getToken(@RequestParam String username, @RequestParam String password) {

        UserEntity userEntity = repository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found."));

        if(passwordEncoder.matches(password, userEntity.getPassword())) {
            Claims claims = new DefaultClaims();
            claims.setExpiration(getExpirationDate());
            claims.setSubject(username);
            claims.put(TokenField.TYPE.name(), userEntity.getType().name());
            claims.put(TokenField.USER_ID.name(), userEntity.getId());
            claims.put(TokenField.USERNAME.name(), userEntity.getUsername());
            claims.put(TokenField.TENANT_CODE.name(), userEntity.getTenantCode());

            return Jwts.builder().signWith(SignatureAlgorithm.HS512, "secret")
                    .setClaims(claims)
                    .compact();
        } else {
            throw new AccessDeniedException("Passwords don't match.");
        }
    }

    private Date getExpirationDate() {
        return Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
    }
}
