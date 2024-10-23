package org.zerock.cleanaido_admin_back.faq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;

@DataJpaTest
public class FAQTests {

FAQRepository faqRepository;
    public FAQ createFAQ(String question, String answer) {
        FAQ faq = FAQ.builder()
                .question(question)
//                .answer(answer)
                .build();

        return faqRepository.save(faq);
    }


}
