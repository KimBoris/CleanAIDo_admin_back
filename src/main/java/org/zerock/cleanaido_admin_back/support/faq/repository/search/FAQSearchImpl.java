package org.zerock.cleanaido_admin_back.support.faq.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.common.entity.QAttachFile;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.entity.QFAQ;

import java.util.List;
import java.util.stream.Collectors;

public class FAQSearchImpl extends QuerydslRepositorySupport implements FAQSearch {

    public FAQSearchImpl() {
        super(FAQ.class);
    }

    @Override
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {
        QFAQ faq = QFAQ.fAQ;

        // 첨부파일 컬렉션
        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<FAQ> query = from(faq);
        query.leftJoin(faq.attachFiles, attachFile).on(attachFile.ord.eq(0));

        query.where(faq.delFlag.isFalse());
        query.orderBy(faq.fno.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<FAQListDTO> results =
                query.select(
                        Projections.bean(
                                FAQListDTO.class,
                                faq.fno,
                                faq.question,
                                faq.delFlag,
                                attachFile.fileName.as("fileName")
                        )
                );

        List<FAQListDTO> dtoList = results.fetch();

        long total = query.fetchCount();

        return PageResponseDTO.<FAQListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


    @Override
    public List<FAQListDTO> convertToDTOList(Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;

        JPQLQuery<FAQ> query = from(faq);
        query.where(faq.delFlag.isFalse());
        query.orderBy(faq.fno.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<FAQ> results = query.fetch();
        System.out.println("================================================================");
        System.out.println(results);

        return results.stream()
                .map(faqEntity -> FAQListDTO.builder()
                        .fno(faqEntity.getFno())
                        .question(faqEntity.getQuestion())
                        .delFlag(faqEntity.isDelFlag())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public Page<FAQ> searchByKeyword(String keyword, Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.question.containsIgnoreCase(keyword));

        JPQLQuery<FAQ> query = from(faq).where(builder);
        getQuerydsl().applyPagination(pageable, query);
        List<FAQ> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}
