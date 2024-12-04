package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "product")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_number", unique = true, nullable = false)
    private Long reviewNumber;

    @Column(name = "review_content", nullable = false, length = 1000)
    private String reviewContent;

    @Column(name = "delFleg")
    @Builder.Default
    private boolean delFlag = false;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ElementCollection
    @Builder.Default
    private Set<ReviewImage> reviewImages = new HashSet<>();

    public void addReviewImage(String filename) {
        reviewImages.add(new ReviewImage(reviewImages.size(), filename));
    }

    public void clearReviewImages() {
        reviewImages.clear();
    }

}

