package org.zerock.cleanaido_admin_back.support.faq.entity;


import jakarta.persistence.*;
import lombok.*;
import org.zerock.cleanaido_admin_back.support.common.entity.AttachFile;

import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private boolean delFlag;
    
    // 첨부파일 컬렉션
    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles = new HashSet<>();

    // 첨부파일 핸들링
    public void addFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFile() {
        attachFiles.clear();
    }



//    public FAQBuilder toBuilder() {
//        return FAQ.builder()
//                .fno(this.fno)
//                .question(this.question)
//                .delFlag(this.delFlag);
//    }
}
