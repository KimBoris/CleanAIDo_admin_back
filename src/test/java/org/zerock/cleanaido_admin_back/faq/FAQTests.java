package org.zerock.cleanaido_admin_back.faq;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQTests {

    @Autowired
    private FAQRepository faqRepository;

    @Test
    public void testList1()
    {
        Pageable pageable = PageRequest.of(0, 10);

        faqRepository.list(pageable);

    }
}
