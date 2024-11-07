package org.zerock.cleanaido_admin_back.support.faq.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.repository.search.FAQSearch;

public interface FAQRepository extends JpaRepository<FAQ, Long>, FAQSearch {

    @Override
    Page<FAQ> searchByKeyword(String keyword, Pageable pageable); // 제목으로 검색 메서드

    @Query("SELECT f FROM FAQ f LEFT JOIN FETCH f.attachFiles WHERE f.fno = :fno")
    FAQ getFAQ(@Param("fno") Long fno);
}