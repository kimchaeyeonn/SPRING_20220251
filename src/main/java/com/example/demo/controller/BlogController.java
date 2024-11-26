package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

// import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.BlogService;
import com.example.demo.model.service.AddArticleRequest;
import org.springframework.data.domain.Page;


@Controller
@ControllerAdvice // 6주차 연습문제
public class BlogController {

    private final BlogService blogService;

    // 의존성 주입: 생성자를 통해 BlogService를 주입받음
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model) {
    //     List<Board> list = blogService.findAll(); // 게시판 전체 리스트
    //     model.addAttribute("boards", list); // 모델에 추가
    //     return "board_list"; // .HTML 연결
    // }

    @GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id) {
        Optional<Board> list = blogService.findById(id); // 선택한 게시판 글
        
        if (list.isPresent()) {
            model.addAttribute("boards", list.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
        } else {
            // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "board_view"; // .HTML 연결
    }
    // 7주차 연습문제
   @GetMapping("/board_edit/{id}") // 게시판 링크 지정
    public String article_edit(Model model, @PathVariable String id) {
        try {
        Long articleId = Long.parseLong(id); // 6주차 연습문제
        Optional<Board> list = blogService.findById(articleId); // 선택한 게시판 글
        List<Board> boards = blogService.findAll(); // 모든 게시글 조회

        if (list.isPresent()) {
            model.addAttribute("board", list.get()); // 존재하면 Article 객체를 모델에 추가
            model.addAttribute("boards", boards); // 모든 게시글 목록을 모델에 추가
        } else {
            // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
            return "/error_page/article_error"; // 오류 처리 페이지로 연결(이름 수정됨)
        }
    // 6주차 연습문제
    } catch (NumberFormatException e) {
        throw new InvalidArticleIdException();
    }
        return "board_edit"; // .HTML 연결
    }
    
    // 7주차 연습문제
    @PutMapping("/api/board_edit/{id}")
    public String updateBoard(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/board_list"; // 글 수정 이후 .html 연결
    }

    @DeleteMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";
    }

    @GetMapping("/board_write")
    public String board_write() {
        return "board_write";
    }

    @PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/board_list"; // .HTML 연결
    }

    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
    //     PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    //     Page<Board> list; // Page를 반환
        
    //     if (keyword.isEmpty()) {
    //         list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
    //     } else {    
    //         list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
    //     }
    //     model.addAttribute("boards", list); // 모델에 추가
    //     model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
    //     model.addAttribute("currentPage", page); // 페이지 번호
    //     model.addAttribute("keyword", keyword); // 키워드
    //     return "board_list"; // .HTML 연결
    // }

    // 8주차 연습문제
    @GetMapping("/board_list")
    public String board_list(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "") String keyword) {
        int pageSize = 3; // 페이지당 게시글 수
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page < Board > list;

        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable);
        } else {
            list = blogService.searchByKeyword(keyword, pageable);
        }

        // 시작 번호 계산
        int startNum = (page * pageSize) + 1;

        model.addAttribute("boards", list);
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startNum", startNum); // 추가된 부분

        return "board_list";
    }


    // 게시판 리스트를 처리하는 메소드
    // @GetMapping("/article_list") // 게시판 링크 지정
    // public String articleList(Model model) {
    //     List<Article> list = blogService.findAll(); // 게시판 리스트
    //     model.addAttribute("articles", list); // 모델에 추가
    //     return "article_list"; // article_list.html 파일로 연결
    // }

    // 5주차 연습문제
    // 글쓰기를 처리하는 메소드
    // @PostMapping("/api/articles")
    // public String addArticle(@ModelAttribute AddArticleRequest request) {
    //     blogService.save(request);
    //     return "redirect:/article_list";
    // }

    // @GetMapping("/article_edit/{id}") // 게시판 링크 지정
    // public String article_edit(Model model, @PathVariable String id) {
    //     try {
    //     Long articleId = Long.parseLong(id); // 6주차 연습문제
    //     Optional<Article> list = blogService.findById(articleId); // 선택한 게시판 글
    //     List<Article> articles = blogService.findAll(); // 모든 게시글 조회

    //     if (list.isPresent()) {
    //         model.addAttribute("article", list.get()); // 존재하면 Article 객체를 모델에 추가
    //         model.addAttribute("articles", articles); // 모든 게시글 목록을 모델에 추가
    //     } else {
    //         // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
    //         return "/error_page/article_error"; // 오류 처리 페이지로 연결(이름 수정됨)
    //     }

    // 6주차 연습문제
    // } catch (NumberFormatException e) {
    //     throw new InvalidArticleIdException();
    // }
    //     return "article_edit"; // .HTML 연결
    // }

    // @PutMapping("/api/article_edit/{id}")
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request);
    //     return "redirect:/article_list"; // 글 수정 이후 .html 연결
    // }

    // @DeleteMapping("/api/article_delete/{id}")
    // public String deleteArticle(@PathVariable Long id) {
    //     blogService.delete(id);
    //     return "redirect:/article_list";
    // }

    // 6주차 연습문제
    @ExceptionHandler(InvalidArticleIdException.class) 
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request 응답 상태 코드
    public String handleInvalidArticleIdException() {
         return "/error_page/article_error2"; 
    }

    static class InvalidArticleIdException extends RuntimeException {
    }
}
