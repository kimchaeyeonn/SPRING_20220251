package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
// import com.example.demo.model.repository.BlogRepository;
import com.example.demo.model.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    // private final BlogRepository blogRepository; // 리포지토리 선언

    // public List<Article> findAll() { // 게시판 전체 목록 조회
    //     return blogRepository.findAll();
    // }

    private final BoardRepository boardRepository; // 리포지토리 선언

    public List<Board> findAll() { // 게시판 전체 목록 조회
        return boardRepository.findAll();
    }

    public Optional<Board> findById(Long id) { // 게시판 특정 글 조회
        return boardRepository.findById(id);
    }
        public void update(Long id, AddBoardRequest request) {
            Optional<Board> optionalBoard = boardRepository.findById(id); // 단일 글 조회
            optionalBoard.ifPresent(board -> { // 값이 있으면
                board.update(request.getTitle(), request.getContent()); // 값을 수정
                boardRepository.save(board); // Board 객체에 저장
            });
        }

        public void delete(Long id) {
            boardRepository.deleteById(id);
        }
    
    public Board save(AddBoardRequest request) {
        Board board = Board.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .build();
        // return boardRepository.save(request.toEntity());
        return boardRepository.save(board);
    }

    // public Article save(AddArticleRequest request){
        // DTO가 없는 경우 이곳에 직접 구현 가능
        // public ResponseEntity<Article> addArticle(@RequestParam String title, @RequestParam String content) {
        // Article article = Article.builder()
        // .title(title)
        // .content(content)
        // .build();

        // 추가구현1 - 작성자
        // Article article = Article.builder()
        //     .title(request.getTitle())
        //     .content(request.getContent())
        //     .author(request.getAuthor()) // 작성자 정보 추가
        //     .build();
        // //return blogRepository.save(request.toEntity());
        // return blogRepository.save(article);
    // }

    // public Optional<Article> findById(Long id) { // 게시판 특정 글 조회
    //     return blogRepository.findById(id);
    // }

        // public void update(Long id, AddArticleRequest request) {
        //     Optional<Article> optionalArticle = blogRepository.findById(id); // 단일 글 조회
        //     optionalArticle.ifPresent(article -> { // 값이 있으면
        //         article.update(request.getTitle(), request.getContent()); // 값을 수정
        //         article.updateAuthor(request.getAuthor()); // 추가구현1 - 작성자
        //         blogRepository.save(article); // Article 객체에 저장
        //     });
        // }

        // public void delete(Long id) {
        //     blogRepository.deleteById(id);
        // }
}