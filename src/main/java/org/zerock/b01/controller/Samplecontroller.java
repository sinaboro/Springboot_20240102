package org.zerock.b01.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class Samplecontroller {

    @GetMapping("/hello")
    public void hello(Model model){
        log.info("hello.................");
        model.addAttribute("msg", "Hello Wrold");
    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model){
        model.addAttribute("arr", new String[]{"AAA","BBB","CCC"});
    }
}
