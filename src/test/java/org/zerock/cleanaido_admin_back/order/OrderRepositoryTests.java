//package org.zerock.cleanaido_admin_back.order;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.test.annotation.Commit;
//import org.zerock.cleanaido_admin_back.order.entity.Order;
//import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;
//
//import java.time.LocalDateTime;
//
//@DataJpaTest
//@Log4j2
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//@Commit
//public class OrderRepositoryTests {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Test
//    public void testInsertDummyOrders() {
//        String[] statuses = {"배송전", "배송중", "배송완료", "취소", "교환", "환불"};
//
//        for (int i = 1; i <= 120; i++) {
//            Order orders = Order.builder()
//                    .productNumber(100 + i)
//                    .customerId("Customer0" + i)
//                    .phoneNumber("010-1234-5679" + i)
//                    .deliveryAddress("부산광역시 진구" + i)
//                    .deliveryMessage("문앞에 두세요 " + i)
//                    .totalPrice(5000 * i)
//                    .trackingNumber("TRACKING" + i)
//                    .orderStatus(statuses[i % statuses.length])
//                    .orderDate(LocalDateTime.now())
//                    .build();
//
//            orderRepository.save(orders);
//        }
//    }
//}
