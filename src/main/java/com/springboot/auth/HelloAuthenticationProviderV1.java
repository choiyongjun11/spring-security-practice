package com.springboot.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloAuthenticationProviderV1 implements AuthenticationProvider { //로그인 인증을 처리하는 방법

    private final HelloUserDetailsServiceV2 helloUserDetailsServiceV2;
    private final PasswordEncoder passwordEncoder;

    public HelloAuthenticationProviderV1(HelloUserDetailsServiceV2 helloUserDetailsServiceV2, PasswordEncoder passwordEncoder) {

        this.helloUserDetailsServiceV2 = helloUserDetailsServiceV2;
        this.passwordEncoder = passwordEncoder;
    }


    //사용자의 인증을 처리, 입력한 ID, PW 검증하고 인증이 성공하면 인증된 객체를 반환
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = authToken.getName(); //사용자가 입력한 아이디를 가져온다.
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));

        try {
            UserDetails userDetails = helloUserDetailsServiceV2.loadUserByUsername(username);
            //비밀번호로 검증 메서드
            String password = userDetails.getPassword();
            verifyCredentials(authToken.getCredentials(), userDetails.getPassword());

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
        } catch (Exception ex) {
            throw new UsernameNotFoundException(ex.getMessage()); // (1) AuthenticationException으로 다시 throw 한다.
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    private void verifyCredentials(Object credentials, String password) {
        if (!passwordEncoder.matches((String)credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }



}
