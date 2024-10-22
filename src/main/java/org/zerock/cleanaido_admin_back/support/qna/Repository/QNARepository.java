package org.zerock.cleanaido_admin_back.support.qna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.support.qna.Repository.search.QNASearch;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

public interface QNARepository extends JpaRepository<Question,Long>, QNASearch {
}
