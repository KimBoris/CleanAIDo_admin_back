package org.zerock.cleanaido_admin_back.support.faq.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @Column(nullable = false)
    private String question;

    private String answer;

    private boolean delFlag;

    public FAQBuilder toBuilder() {
        return FAQ.builder()
                .fno(this.fno)
                .question(this.question)
                .delFlag(this.delFlag);
    }
}
