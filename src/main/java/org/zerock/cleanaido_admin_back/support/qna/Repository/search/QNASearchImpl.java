package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.qna.entity.QQuestion;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.List;


@Log4j2
public class QNASearchImpl extends QuerydslRepositorySupport implements QNASearch {

    public QNASearchImpl() {
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
}

