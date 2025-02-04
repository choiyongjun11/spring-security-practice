package com.springboot.member;

import com.springboot.auth.HelloAuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DBMemberService implements MemberService { //db에 user를 등록하기 위한 구현 클래스

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelloAuthorityUtils authorityUtils; // 회원 가입 시 User 의 권한 정보(Role)를 데이터베이스에 저장하기 위해

    public DBMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }


    @Override
    public Member createMember(Member member) {
        //verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);
        //System.out.println("# Create Member in DB");
        return savedMember;


    }
}
