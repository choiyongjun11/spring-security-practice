//package com.springboot.auth;
//
//import com.springboot.exception.BusinessLogicException;
//import com.springboot.exception.ExceptionCode;
//import com.springboot.member.Member;
//import com.springboot.member.MemberRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//public class HelloUserDetailsServiceV1 implements UserDetailsService {
//    public HelloUserDetailsServiceV1(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
//        this.memberRepository = memberRepository;
//        this.authorityUtils = authorityUtils;
//    }
//
//    private final MemberRepository memberRepository;
//    private final HelloAuthorityUtils authorityUtils;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Member> optionalMember = memberRepository.findByEmail(username);
//        Member findMember = optionalMember.orElseThrow( () -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//
//        //List<GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail());
//        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail());
//
//        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
//    }
//}
