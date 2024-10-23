package org.zerock.cleanaido_admin_back.support.qna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.cleanaido_admin_back.support.qna.Repository.search.QuestionSearch;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Long>, QuestionSearch {


    @Query("""
        SELECT new org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO(
            q.qno, q.title, q.contents, q.writer, a.answerText
            )
        FROM 
        Question q 
        LEFT JOIN Answer a ON a.question = q 
        WHERE q.qno = :qno
        """)
    QuestionReadDTO getQuestion(@Param("qno") Long qno);


}
