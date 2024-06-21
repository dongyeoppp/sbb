package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity     // 엔티티가 데이터테이블을 생성
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 고유 번호

    @Column(length = 200)
    private String subject;     // 게시글 제목

    @Column(columnDefinition = "TEXT")
    private String content;    // 게시글 내용

    private LocalDateTime createDate;       // 시간

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)     // 질문 하나에 답변이 여러개 작성 될 수 있는데 해당 질문이 삭제되면 댓글도 다 삭제되어야 함
    private List<Answer> answerList;        // 댓글 리스트

    @ManyToOne      // 사용자 한명이 질문을 여러개 작성할 수 있음
    private SiteUser author;

    private LocalDateTime modifyDate;
}