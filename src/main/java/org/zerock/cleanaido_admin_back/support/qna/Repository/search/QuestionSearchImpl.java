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
        QQuestion question = QQuestion.question;

        JPQLQuery<Question> query = from(question);
        getQuerydsl().applyPagination(pageable, query);

        List<Question> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

//    @Override
//    public Optional<QuestionReadDTO> getAQuestion(Long qno) {
//        QQuestion question = QQuestion.question;
//        QAnswer answer = QAnswer.answer;
//
//        // JPQLQuery 생성
//        JPQLQuery<Question> query = from(question);
//        query.leftJoin(answer).on(answer.question.eq(question));
//        query.where(question.qno.eq(qno));
//        query.select(question, answer);
//
//        // 결과 조회
//        Question result = query.fetchOne();
//
//        // Question 객체를 QuestionListDTO로 변환
//        QuestionReadDTO questionReadDTO = null;
//        if (result != null) {
//            questionReadDTO = QuestionReadDTO.builder()
//                    .qno(result.getQno())
//                    .title(result.getTitle())
//                    .contents(result.getContents())
//                    .writer(result.getWriter())
//                    .answertext(result)
//                    .build();
//        }
//
//        return Optional.ofNullable(questionReadDTO);
//    }
}

