package com.springboot.auth;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.Member;
import com.springboot.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
    public class HelloUserDetailsServiceV2 implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    public HelloUserDetailsServiceV2(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB에서 불러와서 ? 뭘로 ? username으로 (우리의 경우 아이디는 이메일)
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new HelloUserDetails(findMember);

        //코드가 이전이랑 비교하면 좀 더 간편해진 것을 알수 있다.
        // 기존에 collection<? extend ~ 였다. --> HelloUserDetails 클래스 내부로 포함되었다.

    }

    private final class HelloUserDetails extends Member implements UserDetails { //이 클래스에는 userDetails 인터페이스를 구현하고 있고 Member 엔티티 클래스를 상속받는다.

        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles()); //role 추가 HelloUserDetails가 상속하고 있는 Member(extends Member)에 데이터베이스에서 조회한 List<String> roles 전달한다.
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles()); // 기존에는 this.getEmail이였습니다.  db에 저장된 role 정보로 user 권한 목록 생성
            //다시 member(extends member)에 전달한 Role 정보를 authorityUtils.createAuthorities() 메서드의 파라미터로 전달해서 권한 목록

        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}
