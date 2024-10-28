package org.zerock.cleanaido_admin_back.faq;

import jakarta.persistence.Id;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.common.util.CustomFileUtil;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQRegisterDTO;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;

import java.util.List;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQTests {

    @Autowired
    private FAQRepository faqRepository;
    @Autowired
    private CustomFileUtil customFileUtil;

//    @Test
//    public FAQ createFAQ(String question, String answer) {
//        FAQ faq = FAQ.builder()
//                .question(question)
////                .answer(answer)
//                .build();
//
//        return faqRepository.save(faq);
//    }

    @Test
    public void addFileTest() {
        FAQRegisterDTO dto = new FAQRegisterDTO();
        UploadDTO uploadDTO = new UploadDTO();
        FAQ faq = FAQ.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .build();

        // 첨부파일 저장
        List<String> fileNames = customFileUtil.saveFiles(List.of(uploadDTO.getFiles()));
        fileNames.forEach(faq::addFile);

        faqRepository.save(faq);
    }


}
