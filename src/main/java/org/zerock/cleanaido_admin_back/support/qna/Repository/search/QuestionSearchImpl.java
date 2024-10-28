package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.core.BooleanBuilder;
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

        JPQLQuery<Question> query = from(question).orderBy(question.qno.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<Question> results = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Question> searchByTitleAndContents(String keyword, Pageable pageable) {
        QQuestion question = QQuestion.question;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.or(question.title.containsIgnoreCase(keyword))
                    .or(question.contents.containsIgnoreCase(keyword));
        }

        JPQLQuery<Question> query = from(question).where(builder).orderBy(question.qno.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<Question> results = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Question> searchByWriter(String writer, Pageable pageable) {
        QQuestion question = QQuestion.question;

        JPQLQuery<Question> query = from(question)
                .where(question.writer.containsIgnoreCase(writer))
                .orderBy(question.qno.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<Question> results = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(results, pageable, total);
    }
}




