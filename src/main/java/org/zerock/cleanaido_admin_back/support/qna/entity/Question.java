package org.zerock.cleanaido_admin_back.support.qna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.zerock.cleanaido_admin_back.support.common.entity.AttachFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    private String title;

    private String contents;

    private String writer;

    private boolean answered;

    @CreationTimestamp
    @Column(updatable = false) // 생성 시에만 값이 설정되고 수정 시에는 변경되지 않도록 설정
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles = new HashSet<>();

//    public void addFile(String filename){
//        attachFiles.add(new AttachFile(attachFiles.size(), filename));
//    }
//    public void clearFiles(){
//        attachFiles.clear();
//    }
}
