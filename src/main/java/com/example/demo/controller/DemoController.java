package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.TestService;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    @Autowired
    TestService testService;

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

    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {
        model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
        model.addAttribute("data2", "태그의 속성 값");
        model.addAttribute("link", 01);
        model.addAttribute("name", "홍길동");
        model.addAttribute("para1", "001");
        model.addAttribute("para2", 002);
        return "thymeleaf_test1";
    }

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        TestDB test = testService.findByName("홍길동");
        model.addAttribute("data4", test);

        // 4주차 연습문제
        TestDB test2 = testService.findByName("아저씨");
        TestDB test3 = testService.findByName("꾸러기");
        model.addAttribute("data5", test2);
        model.addAttribute("data6", test3);

        System.out.println("데이터 출력 디버그 : " + test + test2 + test3);
        return "testdb";
    }
}
