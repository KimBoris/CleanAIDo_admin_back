package org.zerock.cleanaido_admin_back.support.qna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.support.qna.Repository.search.QuestionSearch;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Long>, QuestionSearch {

}
