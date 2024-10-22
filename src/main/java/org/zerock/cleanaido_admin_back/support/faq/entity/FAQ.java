package org.zerock.cleanaido_admin_back.support.faq.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;
}
