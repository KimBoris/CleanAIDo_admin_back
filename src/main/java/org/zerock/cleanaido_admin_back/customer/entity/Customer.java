package org.zerock.cleanaido_admin_back.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zerock.cleanaido_admin_back.order.entity.Order;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name="customer_pw")
    private String customerPw;

    @Column(name="name")
    private String customerName;

    @Column(name="birth_date")
    private LocalDate birthDate;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="updated_date")
    private Timestamp updatedDate;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="delFlag")
    private boolean delFlag;

    @Column(name="address")
    private String address;

    @Column(name="profile_image_url")
    private String profileImageUrl;

    @Column(name="fcm_token")
    private String fcmToken;
//
//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Order> orders;


    public void toggleDelflag()
    {
        this.delFlag = !this.delFlag;
    }
}
