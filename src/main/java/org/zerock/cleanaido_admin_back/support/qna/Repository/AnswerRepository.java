package org.zerock.cleanaido_admin_back.support.qna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.cleanaido_admin_back.support.qna.dto.AnswerDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Answer;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.question.qno = :qno")
    Answer findAnswerByQno(@Param("qno") Long qno);

}
