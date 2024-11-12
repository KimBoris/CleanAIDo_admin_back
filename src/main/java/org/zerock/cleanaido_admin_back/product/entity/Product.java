package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.zerock.cleanaido_admin_back.product.entity.QCategory.category;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_number", unique = true, nullable = false)
    private Long pno;

    @Column(name = "product_code", nullable = false, length = 100)
    private String pcode;

    @Column(name = "product_name", nullable = false, length = 100)
    private String pname;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false) // 생성 시에만 값이 설정되고 수정 시에는 변경되지 않도록 설정
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedAt;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releasedAt;

    @Column(name = "product_status", nullable = false, length = 50)
    @Builder.Default
    private String pstatus = "판매중";

    @Column(name = "tags", length = 100)
    private String ptags;

    @Column(name = "user_id")
    private String sellerId;

    @ElementCollection
    @Builder.Default
    private Set<ImageFiles> imageFiles = new HashSet<>();

    public void addImageFile(String filename, boolean type) {
        imageFiles.add(new ImageFiles(imageFiles.size(), filename, type));
    }

    public void clearImageFile() {
        imageFiles.clear();
    }

    @ElementCollection
    @Builder.Default
    private Set<UsageImageFiles> usageImageFiles = new HashSet<>();

    public void addUsingImageFile(String filename) {
        usageImageFiles.add(new UsageImageFiles(usageImageFiles.size(), filename));
    }

    public void clearUsingImageFile() {
        usageImageFiles.clear();
    }

}
