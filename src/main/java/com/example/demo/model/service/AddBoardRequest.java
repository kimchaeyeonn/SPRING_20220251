package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import com.example.demo.model.domain.Board;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddBoardRequest {
    private String title;
    private String content;
    // private String author; // 추가구현1 - 작성자
    public Board toEntity(){ // Article 객체 생성
        return Board.builder()
            .title(title)
            .content(content)
            // .author(content) // 추가구현1 - 작성자
            .build();
    }
}