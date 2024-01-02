package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private  final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("----------------------------loadUserByUsername--------------------------------------");
        log.info(username);

        //Member member =  memberRepository.getWithRoles(username)
        //        .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다.!!!!!!"));

        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        Member member = result.get();

        return new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.getDel(),
                member.getSocial(),
                member.getRoleSet().stream().map(role->new SimpleGrantedAuthority(
                        "ROLE_"+ role.name()
                )).collect(Collectors.toList())
        );
    }
}
