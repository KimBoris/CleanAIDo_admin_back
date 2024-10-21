package org.zerock.cleanaido_admin_back.support.faq.repository.search;


import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.entity.QFAQ;

@Log4j2
public class FAQSearchImpl extends QuerydslRepositorySupport implements FAQSearch {
    public FAQSearchImpl() {
        super(FAQ.class);
    }

    @Override
    public Page<FAQ> list(Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;

        JPQLQuery<FAQ> query = from(faq);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<FAQ> faqQuery = query.select(
                faq
        );

        faqQuery.fetch();

        return null;
    }

}
