package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.QAnswer;
import org.zerock.cleanaido_admin_back.support.qna.entity.QQuestion;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.List;
import java.util.Optional;


@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

    public QuestionSearchImpl() {
        super(Question.class);
    }

    @Override
    public Page<Question> list(Pageable pageable) {
        try {
            QQuestion question = QQuestion.question;

            JPQLQuery<Question> query = from(question);

            // 페이징 처리 적용
            getQuerydsl().applyPagination(pageable, query);

            // 결과 목록을 가져옵니다.
            List<Question> results = query.fetch();

            // 총 레코드 수 계산
            long total = query.fetchCount();

            // 결과 반환
            return new PageImpl<>(results, pageable, total);
        } catch (IllegalArgumentException ex) {
            log.error("잘못된 페이지 정보입니다: {}", ex.getMessage());
            throw new IllegalArgumentException("잘못된 페이지 정보입니다.");
        } catch (Exception ex) {
            log.error("질문 목록을 불러오는 중 오류가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("질문 목록을 불러오는 중 오류가 발생했습니다.");
        }
    }
}

