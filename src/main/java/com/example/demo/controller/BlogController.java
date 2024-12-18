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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    public String board_view(Model model, @PathVariable Long id, HttpSession session) {
        try {
            // 게시글 조회
            Optional<Board> list = blogService.findById(id); // 선택한 게시판 글

            if (list.isPresent()) {
                Board board = list.get();
                model.addAttribute("boards", board); // 게시글 데이터 모델에 추가

                // 10주차 연습문제 - 세션에서 로그인된 사용자 아이디 가져오기
                String userName = (String) session.getAttribute("userName");

                // 10주차 연습문제 - 작성자만 수정 및 삭제 가능
                if (userName != null && userName.equals(board.getUser())) {
                    model.addAttribute("canEdit", true);  // 작성자만 수정 가능
                    model.addAttribute("canDelete", true); // 작성자만 삭제 가능
                } else {
                    // 11주차 연습문제
                    model.addAttribute("canEdit", false);  // 다른 사용자는 수정 불가
                    model.addAttribute("canDelete", false); // 다른 사용자는 삭제 불가
                }
            } else {
                model.addAttribute("error", "게시글을 찾을 수 없습니다.");
                return "/error_page/article_error"; // 오류 처리 페이지로 연결
            }

            return "board_view"; // 게시글 상세보기 페이지로 이동
        } catch (Exception e) {
            // 11주차 연습문제
            e.printStackTrace();
            model.addAttribute("error", "오류가 발생했습니다: " + e.getMessage());
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
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

    // @GetMapping("/board_write")
    // public String board_write() {
    //     return "board_write";
    // }

    // 10주차 연습문제
    @GetMapping("/board_write")
    public String board_write(HttpServletRequest request, Model model) {
        // 세션에서 사용자 이름 가져오기
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            String userName = (String) session.getAttribute("name"); // 세션에서 이름 가져오기
    
            // 이름을 모델에 추가하여 Thymeleaf에서 사용할 수 있도록 설정
            model.addAttribute("name", userName);
        }
    
        return "board_write"; // .HTML 연결
    }

    // 10주차 연습문제
    @PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddArticleRequest request, HttpSession session) {
        String userName = (String) session.getAttribute("userName"); // 세션에서 사용자 ID를 가져옴
        if (userName != null) {
            request.setUser(userName); // 작성자로 로그인한 사용자 설정
        } else {
            request.setUser("GUEST"); // 로그인되지 않은 경우 GUEST로 설정
        }
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
    @GetMapping("/board_list")
    public String boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session,
            // 11주차 연습문제
            Model model) {
        // 세션 유효성 확인
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/member_login"; // 세션이 없으면 로그인 페이지로 이동
        }
    
        // 세션에서 사용자 정보 가져오기
        String userName = (String) session.getAttribute("userName");
        String email = (String) session.getAttribute("email");
        model.addAttribute("userName", userName);
        model.addAttribute("email", email);
    
        // 8주차 연습문제 - 게시판 데이터 처리(시작 번호 계산)
        int pageSize = 5;
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<Board> boards = keyword.isEmpty()
                ? blogService.findAll(pageable)
                : blogService.searchByKeyword(keyword, pageable);
    
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boards.getTotalPages());
        model.addAttribute("keyword", keyword);
    
        return "board_list"; // `board_list.html` 템플릿 반환
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
