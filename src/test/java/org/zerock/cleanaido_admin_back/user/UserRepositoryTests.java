package org.zerock.cleanaido_admin_back.user;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsertDummyUsers() {
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .userId(String.format("user%02d", i)) // user01, user02 형식으로 ID 설정
                    .password("password" + i)
                    .businessNumber("123-45-6789" + i)
                    .businessName("Business Name " + i)
                    .businessType("Type" + i)
                    .ownerName("Owner Name " + i)
                    .businessAddress("Address " + i)
                    .businessStatus("Active")
                    .businessCategory("Category " + i)
                    .storeName("Store " + i)
                    .commerceLicenseNum("CLN-" + i)
                    .businessLicenseFile("license" + i + ".pdf")
                    .originAddress("Origin Address " + i)
                    .contactNumber("010-1234-567" + i)
                    .accountNumber("Account" + i)
                    .userStatus("Active")
                    .delFlag(false)
                    .adminRole(i == 1 || i == 2)
                    .createDate(LocalDateTime.now())
                    .build();

            userRepository.save(user);
        }
    }

    @Test
    public void testUpdateUser() {
        Optional<User> optionalUser = userRepository.findById("user01");
        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();
        user.setBusinessName("Updated Business Name");


        userRepository.save(user);


        User updatedUser = userRepository.findById("user01").orElseThrow();
        assertThat(updatedUser.getUpdatedDate()).isNotNull();
    }
}
