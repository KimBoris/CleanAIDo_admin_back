package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.user.entity.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "seller") // 순환 참조 방지
@EqualsAndHashCode(exclude = "seller")
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
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedAt;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releasedAt;

    @Column(name = "product_status", nullable = false, length = 50)
    @Builder.Default
    private String pstatus = "selling";

    @Column(name = "use_case", nullable = false)
    private String puseCase;

    @Column(name = "used_item", nullable = false)
    private String pusedItem;

    @Column(name = "tags", length = 100)
    private String ptags;

    @ManyToOne(fetch = FetchType.LAZY) // Many products can belong to one user
    @JoinColumn(name = "user_id") // Join with the 'user_id' column in 'users' table
    private User seller;

    @ElementCollection
    @Builder.Default
    private Set<ImageFiles> imageFiles = new HashSet<>();

    @ElementCollection
    @Builder.Default
    private Set<UsageImageFile> usageImageFiles = new HashSet<>();

    public void addImageFile(String filename, boolean type) {
        imageFiles.add(new ImageFiles(imageFiles.size(), filename, type));
    }

    public void clearImageFile() {
        imageFiles.clear();
    }

    public void addUsingImageFile(String filename) {
        usageImageFiles.add(new UsageImageFile(usageImageFiles.size(), filename));
    }

    public void clearUsingImageFile() {
        usageImageFiles.clear();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_number", nullable = false)
    private Category category;


}
