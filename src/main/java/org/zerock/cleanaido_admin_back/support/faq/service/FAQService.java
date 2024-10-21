package org.zerock.cleanaido_admin_back.support.faq.service;


import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;


@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;

    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO){

        log.info("list를 떙겨와 ");
        if(pageRequestDTO.getPage() < 0)
        {
           log.info("페이지수가 음수 입니다. ");
        }

        return null;
    }
}
