//package org.zerock.cleanaido_admin_back.user;
//
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.zerock.cleanaido_admin_back.user.entity.User;
//import org.zerock.cleanaido_admin_back.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//@Log4j2
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class UserRepositoryTests {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testInsertDummyUsers() {
//            User user = User.builder()
//                    .userId(String.format("aaa@bbb.com")) // user01, user02 형식으로 ID 설정
//                    .password("$2a$12$OalSjKRwjseCYWJYOe4CXeTk2WQv2Jtie6oXIaIa2u9npYLCYZH3W")
//                    .businessNumber("123-45-6789")
//                    .businessName("Business Name ")
//                    .businessType("Type")
//                    .ownerName("Owner Name ")
//                    .businessAddress("Address ")
//                    .businessStatus("Active")
//                    .businessCategory("Category ")
//                    .storeName("Store ")
//                    .commerceLicenseNum("CLN-")
//                    .businessLicenseFile("license.pdf")
//                    .originAddress("Origin Address ")
//                    .contactNumber("010-1234-5678")
//                    .accountNumber("Account")
//                    .userStatus("Active")
//                    .delFlag(false)
//                    .adminRole(true)
//                    .createDate(LocalDateTime.now())
//                    .build();
//
//            userRepository.save(user);
//        }
//
//
//    @Test
//    public void testUpdateUser() {
//        Optional<User> optionalUser = userRepository.findById("user01");
//        assertThat(optionalUser).isPresent();
//
//        User user = optionalUser.get();
//        user.setBusinessName("Updated Business Name");
//
//
//        userRepository.save(user);
//
//
//        User updatedUser = userRepository.findById("user01").orElseThrow();
//        assertThat(updatedUser.getUpdatedDate()).isNotNull();
//    }
//}
