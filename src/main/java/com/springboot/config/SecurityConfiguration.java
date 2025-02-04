package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
// 처음 화면이 https 로 되어 있으며 무조건 user는 lucky@cat.house, 비밀번호 1234 를 입력해야지 홈페이지에 접속이 가능합니다. (가입 불가)
//    @Bean
//    public UserDetailsManager userDetailsManger() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("lucky@cat.house")
//                .password("1234")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//
//    }

    //이메일이나 비밀번호가 틀리면 "로그인 인증에 실패 했습니다" 사용자에게 알림을 해줍니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // --> 없으면 h2 console 접근이 제한됩니다.
                .and()
                .csrf().disable() //1 CSRF 공격에 대한 SPRING SECURITY 설정 비활성
                .formLogin() //2 폼 로그인 방식으로 기본적인 인증
                .loginPage("/auths/login-form") //3 커스텀 로그인 페이지를 사용하도록 설정
                .loginProcessingUrl("/process_login") //4 로그인 인증 요청을 수행할 요청 url 지정
                .failureUrl("/auths/login-form?error") //5 로그인 인증에 실패할 경우 어떤 화면으로 리다이렉트 할 것 인지 지정. ? = 쿼리 파라미터를 의미 합니다.
                .and()//6
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/auths/access-denied") //403 에러 발생 시, 파라미터로 지정한 url로 리다이렉트 되도록한다.
                .and()
                .authorizeHttpRequests(authorize -> authorize //2
                        .antMatchers("/orders/**").hasRole("ADMIN") //ADMIN Role을 부여받은 사용자만 /orders로 시작하는 모든 url에 접근할 수 있다는 의미다.
                        .antMatchers("/members/my-page").hasRole("USER") //3-2
                        .antMatchers("/**").permitAll() //3-3  URL 이외의 나머지 모든 URL은 Role에 상관없이 접근이 가능함
                    );
        return http.build();

    }
    //http 로 접속이 되고 가입도 가능한 페이지가 열립니다. 하지만 아무리 회원 가입을 해도 가입한 계정으로 로그인이 안된다는 것을 알수 있습니다.
    // 아래에 있는 user 변수에 넣어져 있는 kevin@gmail.com 으로 로그인을 해야합니다.
    //@Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("kevin@gmail.com")
//                        .password("1111")
//                        .roles("USER")
//                        .build();
//
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin@gmail.com")
//                        .password("2222")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

//    @Bean
//    public UserDetailsManager userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("kevin@gmail.com")
//                        .password("1111")
//                        .roles("USER")
//                        .build();
//
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin@gmail.com")
//                        .password("2222")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user,admin);
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();  // (1-1)
    }



}
