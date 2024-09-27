package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {
    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }
    // 2주차 연습문제
    @GetMapping("/hello2") // 전송 방식 GET
    public String hello2(Model model) {
        model.addAttribute("data1", " 홍길동님.");
        model.addAttribute("data2", " 반갑습니다.");
        model.addAttribute("data3", " 오늘.");
        model.addAttribute("data4", " 날씨는.");
        model.addAttribute("data5", " 매우 좋습니다.");
        return "hello2"; 
    }

    @GetMapping("/about_detailed")
    public String about() {
        return "about_detailed";
    }
}
