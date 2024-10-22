package org.zerock.cleanaido_admin_back.support.faq.repository.search;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.entity.QFAQ;

import java.util.List;

public class FAQSearchImpl extends QuerydslRepositorySupport implements FAQSearch {

    public FAQSearchImpl() {
        super(FAQ.class);
    }

    @Override
    public Page<FAQ> list(Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;

        JPQLQuery<FAQ> query = from(faq);
        query.where(faq.delFlag.isFalse());
        getQuerydsl().applyPagination(pageable, query);

        List<FAQ> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }


}
