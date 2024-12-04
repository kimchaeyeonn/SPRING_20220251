package com.example.demo.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter // setter는 없음(무분별한 변경 x)
@Entity // 아래 객체와 DB 테이블을 매핑. JPA가 관리
@Table(name = "board") // 테이블 이름을 지정. 없는 경우 클래스이름으로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 생성자 접근 방지

public class Board {
    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;

    @Column(name = "title", nullable = false) // null x
    private String title = "";

    @Column(name = "content", nullable = false)
    private String content = "";

    // @Column(name = "author", nullable = false) // 작성자 정보 추가
    // private String author = ""; // 추가구현1 - 작성자 

    @Column(name = "user", nullable = false)
    private String user = "";

    @Column(name = "newdate", nullable = false)
    private String newdate = "";
    
    @Column(name = "count", nullable = false)
    private String count = "";

    @Column(name = "likec", nullable = false)
    private String likec = "";
    
    @Builder // 생성자에 빌더 패턴 적용(불변성)
    public Board(String title, String content, String user, String newdate, String count, String likec) {
        this.title = title;
        this.content = content;
        // 7주차 연습문제
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        // this.author = author; // 추가구현1 - 작성자
    }

    // 10주차 연습문제
    public String getUser() {
        return user;
    }
    
    // 10주차 연습문제
    public void setUser(String User) {
        this.user = user;
    }

    public void update(String title, String content, String user, String newdate, String count, String likec) { // 현재 객체 상태 업데이트
        this.title = title;
        this.content = content;
        }

    // 추가구현1 - 작성자
    // public void updateAuthor(String author) {
    //     this.author = author;
    // }
}

