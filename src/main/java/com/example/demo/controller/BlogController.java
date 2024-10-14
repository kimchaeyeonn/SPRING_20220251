package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.model.domain.Article;
import com.example.demo.model.service.BlogService;

@Controller
public class BlogController {

    private final BlogService blogService;

    // 의존성 주입: 생성자를 통해 BlogService를 주입받음
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // 게시판 리스트를 처리하는 메소드
    @GetMapping("/article_list") // 게시판 링크 지정
    public String articleList(Model model) {
        List<Article> list = blogService.findAll(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // article_list.html 파일로 연결
    }
}
