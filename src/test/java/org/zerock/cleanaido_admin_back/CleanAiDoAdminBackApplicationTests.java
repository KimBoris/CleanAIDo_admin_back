package org.zerock.cleanaido_admin_back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;

@SpringBootTest
class CleanAiDoAdminBackApplicationTests {


    @Autowired
    ProductRepository repository;

    @Autowired
    FAQRepository faqRepository;

    @Test
    @Transactional
    @Commit
    public void testDummies() {
        for(int i = 0; i < 10; i++)
        {
            Product product = Product.builder()
                    .name("User"+i)
                    .build();

            repository.save(product);


        }
    }
    @Test
    @Transactional
    @Commit
    public void testDummiesFAQ() {
        for(int i = 0; i < 10; i++)
        {
            FAQ faq = FAQ.builder()
                    .question("TITLE "+i)
                    .answer("DES "+"TITLE "+i)
                    .build();

            faqRepository.save(faq);


        }
    }
}
