//package com.springboot.config;
//
//import com.springboot.member.DBMemberService;
//import com.springboot.member.InMemoryMemberService;
//import com.springboot.member.MemberRepository;
//import com.springboot.member.MemberService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
//
//@Configuration
//public class JavaConfiguration {
////    @Bean
////    public MemberService inMemoryMemberService(UserDetailsManager userDetailsManager,
////                                               PasswordEncoder passwordEncoder) {
////
////        return new InMemoryMemberService(userDetailsManager, passwordEncoder);
////    }
//    //InMemoryMemberService 클래스는 데이터베이스 연동 없이 메모리에 Spring Security의 User를 등록해야 하므로 `UserDetailsManager` 객체가 필요합니다.
//    //또한 User 등록 시, 패스워드를 암호화한 후에 등록해야 하므로 Spring Security에서 제공하는 `PasswordEncoder` 객체가 필요합니다.
//
//    public MemberService dbMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
//
//        return new DBMemberService(memberRepository, passwordEncoder);
//
//    }
//}
