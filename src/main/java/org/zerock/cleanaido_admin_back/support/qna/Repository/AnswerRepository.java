package org.zerock.cleanaido_admin_back.support.qna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.support.qna.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
