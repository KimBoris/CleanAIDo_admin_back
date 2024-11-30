package org.zerock.cleanaido_admin_back.user.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO { //판매자 조회
    private String userId;
    private String password;
    private String businessName;
    private String businessType;
    private String ownerName;
    private String businessAddress;
    private String businessStatus;
    private String businessCategory;
    private String storeName;
    private String commerceLicenseNum;
    private String businessLicenseFile;
    private String originAddress;
    private String contactNumber;
    private String accountNumber;
    private String userStatus;
    private boolean delFlag;
    private boolean adminRole;
    private LocalDateTime createDate;
}
