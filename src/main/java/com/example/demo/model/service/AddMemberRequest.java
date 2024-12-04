package com.example.demo.model.service;

import com.example.demo.model.domain.Member;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data 
public class AddMemberRequest {
    // 9주차 연습문제
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]+$")
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
    private String password;

    @Pattern(regexp = "^\\d+$")
    @Min(value = 19)
    @Max(value = 90)
    private String age;

    @NotEmpty
    private String mobile;

    @NotEmpty
    private String address;

    // private String name;
    // private String email;
    // private String password;
    // private String age;
    // private String mobile;
    // private String address;

    public Member toEntity(){ // Member 생성자를 통해 객체 생성
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            .age(age)
            .mobile(mobile)
            .address(address)
            .build();
    }
}
