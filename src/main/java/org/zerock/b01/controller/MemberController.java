package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")
    public void loginGET(@RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "logout", required = false) String logout){
        log.info("login get.....................");
        log.info("logout: " + logout);
    }


    @GetMapping("/join")
    public void joinGET(){
        log.info("join get........");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO,
                           RedirectAttributes rttr){
        log.info("join post..........");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {

            rttr.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        rttr.addFlashAttribute("result", "success");

        return "redirect:/board/list";
    }
}
