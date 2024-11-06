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
