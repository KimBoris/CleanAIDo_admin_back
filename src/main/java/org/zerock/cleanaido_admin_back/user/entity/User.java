package org.zerock.cleanaido_admin_back.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zerock.cleanaido_admin_back.product.entity.Product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    @Id
    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @Column(name = "user_pw", length = 255, nullable = false)
    private String password;

    @Column(name = "business_number", length = 20)
    private String businessNumber;

    @Column(name = "business_name", length = 255)
    private String businessName;

    @Column(name = "business_type", length = 20)
    private String businessType;

    @Column(name = "owner_name", length = 255)
    private String ownerName;

    @Column(name = "business_address", length = 255)
    private String businessAddress;

    @Column(name = "business_status", length = 255)
    private String businessStatus;

    @Column(name = "business_category", length = 255)
    private String businessCategory;

    @Column(name = "store_name", length = 255)
    private String storeName;

    @Column(name = "commerce_license_num", length = 255)
    private String commerceLicenseNum;

    @Column(name = "business_license_file", length = 255)
    private String businessLicenseFile;

    @Column(name = "origin_address", length = 255)
    private String originAddress;

    @Column(name = "contact_number", length = 255)
    private String contactNumber;

    @Column(name = "account_number", length = 255)
    private String accountNumber;

    @Column(name = "user_status", length = 255)
    private String userStatus;

    @Column(name = "delFlag", nullable = false)
    private boolean delFlag;

    @Column(name = "admin_role", nullable = false)
    private boolean adminRole;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();
}