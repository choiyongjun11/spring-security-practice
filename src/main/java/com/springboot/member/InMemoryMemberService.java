package com.springboot.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryMemberService implements MemberService { //memberservice 인터페이스의 구현 클래스

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member createMember(Member member) { //회원 가입
        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name()); //User의 권한 목록을 List<GrantedAuthority>로 생성하고 있다.

        String encryptedPassword = passwordEncoder.encode(member.getPassword()); //USER의 패스워드를 암호화 하고 있다.

        UserDetails userDetails = new User(member.getEmail(),encryptedPassword, authorities);

        userDetailsManager.createUser(userDetails); //User 등록

        return member;

    }

    private List<GrantedAuthority> createAuthorities(String... roles) {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
    //simpleGrantedAuthority를 사용해 Role 베이스 형태의 권한을 지정할 때 'ROLE_'+권한 명 형태로 지정해 주어야 합니다.
    //simpleGrantedAuthortiy 객체를 생성한 후 LIST 형태로 리턴해줍니다.
}
