package org.zerock.cleanaido_admin_back.support.faq.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQRegisterDTO;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.FAQRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class FAQService {
    private final FAQRepository faqRepository;

    public PageResponseDTO<FAQListDTO> listFAQ(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<FAQ> faqPage = faqRepository.list(pageable);

        List<FAQListDTO> dtoList = faqPage.getContent().stream()
                .map(faq -> FAQListDTO.builder()
                        .fno(faq.getFno())
                        .question(faq.getQuestion())
                        .build())
                .collect(Collectors.toList());

        log.info("==================================================");
        log.info(dtoList);

        return new PageResponseDTO<>(dtoList, pageRequestDTO, faqPage.getTotalElements());
    }


    public void updateFAQ(Long fno, FAQRegisterDTO faqDTO)
    {
        FAQ faq = faqRepository.findById(fno)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found" + fno));

        


        faqRepository.save(faq);
    }
//
//    public void updateFAQ(Long fno, FAQRegisterDTO faqDTO) {
//        FAQ faq = faqRepository.findById(fno)
//                .orElseThrow(() -> new EntityNotFoundException("FAQ not found " + fno));
//
//        FAQ updatedFAQ =faq.toBuilder()
//                .question(faqDTO.getQuestion())
//                .answer
//                .build();
//
//
//        faqRepository.save(faq);
//    }


    public void deleteFAQ(Long fno) {
        FAQ faq = faqRepository.findById(fno)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found " + fno));
        faq.setDelFlag(true);
        faqRepository.save(faq);
    }
}
