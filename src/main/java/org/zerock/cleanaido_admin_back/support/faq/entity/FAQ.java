package org.zerock.cleanaido_admin_back.support.faq.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String question;

    private String answer;
}
