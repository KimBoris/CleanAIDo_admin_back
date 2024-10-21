package org.zerock.cleanaido_admin_back.support.faq.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;

public interface FAQSearch {

    Page<FAQ> list(Pageable pageable);


}
