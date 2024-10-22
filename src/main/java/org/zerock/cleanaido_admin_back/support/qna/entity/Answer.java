package org.zerock.cleanaido_admin_back.support.qna.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(exclude = "question")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    private String answerText;

    @OneToOne(fetch = FetchType.LAZY)
    private Question question;
}
