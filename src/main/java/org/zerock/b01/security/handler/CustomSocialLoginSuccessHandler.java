package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b01.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("---------------------------------------------");

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO)authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getMpw();

        if(memberSecurityDTO.isSocial() &&
                (memberSecurityDTO.getMpw().equals("1111") || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))){
            response.sendRedirect("/member/modify");
        }else{
            response.sendRedirect("/board/list");
        }

    }
}
