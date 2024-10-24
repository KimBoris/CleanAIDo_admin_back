package org.zerock.cleanaido_admin_back.support.faq.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
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
    public Page<FAQ> list(Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;

        JPQLQuery<FAQ> query = from(faq);
        query.where(faq.delFlag.isFalse());
        query.orderBy(faq.fno.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<FAQ> results = query.fetch();

        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public FAQ getFAQ(Long fno) {
        QFAQ faq = QFAQ.fAQ;
//        return from(faq).where(faq.fno.eq(fno).and(faq.delFlag.isFalse())).fetchOne();
        return from(faq).where(faq.fno.eq(fno)).fetchOne();
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
    public Page<FAQ> searchByTitle(String keyword, Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;
        JPQLQuery<FAQ> query = from(faq);
        query.where(faq.question.containsIgnoreCase(keyword)
                .and(faq.delFlag.isFalse())); // 검색어와 delFlag 조건

        query.orderBy(faq.fno.desc());

        List<FAQ> results = getQuerydsl().applyPagination(pageable, query).fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
