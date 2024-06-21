package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;     // 고유번호

    @Column(columnDefinition = "TEXT")
    private String content;     // 댓글 내용

    private LocalDateTime createDate;       // 시간

    @ManyToOne      // N:1 관계를 표현, 답변과 질문은 N:1 관계를 가짐
    private Question question;      // 해당 댓글에 대한 질문

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;
}
