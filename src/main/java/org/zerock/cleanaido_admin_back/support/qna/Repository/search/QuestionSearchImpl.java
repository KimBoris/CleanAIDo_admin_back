package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionDTO;
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
        QQuestion question = QQuestion.question;

        JPQLQuery<Question> query = from(question);
        getQuerydsl().applyPagination(pageable, query);

        List<Question> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Optional<QuestionDTO> getAQuestion(Long qno) {
        QQuestion question = QQuestion.question;

        // JPQLQuery 생성
        JPQLQuery<Question> query = from(question)
                .where(question.qno.eq(qno));

        // 결과 조회
        Question result = query.fetchOne();

        // Question 객체를 QuestionListDTO로 변환
        QuestionDTO questionDTO = null;
        if (result != null) {
            questionDTO = QuestionDTO.builder()
                    .qno(result.getQno())
                    .title(result.getTitle())
                    .contents(result.getContents())
                    .writer(result.getWriter())
                    .answered(result.isAnswered())
                    .createdAt(result.getCreatedAt())
                    .updatedAt(result.getUpdatedAt())
                    .build();
        }

        return Optional.ofNullable(questionDTO);
    }
}

