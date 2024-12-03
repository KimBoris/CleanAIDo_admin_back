package org.zerock.cleanaido_admin_back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;
import org.zerock.cleanaido_admin_back.support.qna.Repository.QuestionRepository;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.time.LocalDateTime;

@SpringBootTest
class CleanAiDoAdminBackApplicationTests {


    @Autowired
    ProductRepository repository;

    @Autowired
    FAQRepository faqRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @Transactional
    @Commit
    public void testDummies() {
        LocalDateTime releaseDate = LocalDateTime.of(2024, 11, 26, 11, 0, 58);

        for(int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .pcode("z1kdf" + i)
                    .pname("product" + i)
                    .price(10000 + i * 1000)
                    .quantity(i)
                    .releasedAt(releaseDate)
                    .pstatus("판매중")
                    .ptags("액체세제,인체무해성분")
//                    .sellerId("s30")
                    .build();

            repository.save(product);
        }
    }

    @Test
    @Transactional
    @Commit
    public void testDummiesFAQ() {
        for(int i = 0; i < 100; i++)
        {
            FAQ faq = FAQ.builder()
                    .question("TITLE "+i)
//                    .answer("DES "+"TITLE "+i)
                    .build();

            faqRepository.save(faq);


        }
    }

    @Test
    @Transactional
    @Commit
    public void testDummiesQuestion() {
        for(int i = 0; i < 180; i++)
        {
            Question qus = Question.builder()
                    .title("TITLE "+i)
                    .contents("DES "+"TITLE "+i)
                    .writer("user"+i)
                    .build();

            questionRepository.save(qus);


        }
    }
    
}
