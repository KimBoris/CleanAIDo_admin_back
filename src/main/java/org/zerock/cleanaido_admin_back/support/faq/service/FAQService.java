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
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQReadDTO;
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
//        Page<FAQ> faqPage = faqRepository.list(pageable);

        List<FAQListDTO> dtoList = faqRepository.convertToDTOList(pageable);

        long totalElements = faqRepository.count();

        return new PageResponseDTO<>(dtoList, pageRequestDTO, totalElements);
    }

    public Long registerFAQ(FAQRegisterDTO dto) {
        FAQ faq = FAQ.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .delFlag(dto.isDelFlag())
                .build();

        faqRepository.save(faq);

        return faq.getFno();
    }

    public FAQReadDTO readFAQ(Long fno) {
        FAQ faq = faqRepository.getFAQ(fno);
        if (faq == null) {
            throw new EntityNotFoundException("FAQ not found" + fno);
        }

        return FAQReadDTO.builder()
                .pno(faq.getFno())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .delFlag(faq.isDelFlag())
                .build();
    }


    public void deleteFAQ(Long fno) {
        FAQ faq = faqRepository.findById(fno)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found " + fno));
        faq.setDelFlag(true);
        faqRepository.save(faq);
    }
}
