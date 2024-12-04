package org.zerock.cleanaido_admin_back.customer.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListDTO {
        private String customerId;
        private String customerPw;
        private String customerName;
        private LocalDate birthDate;
        private Timestamp createDate;
        private Timestamp updatedDate;
        private String phoneNumber;
        private boolean delFlag;
        private String address;
        private String profileImageUrl;
}
