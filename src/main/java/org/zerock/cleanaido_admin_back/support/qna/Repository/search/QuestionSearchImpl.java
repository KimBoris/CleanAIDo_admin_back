package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.common.entity.QAttachFile;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
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

//    @Override
//    public Page<Question> list(Pageable pageable) {
//        QQuestion question = QQuestion.question;
//
//        JPQLQuery<Question> query = from(question).orderBy(question.qno.desc());
//        getQuerydsl().applyPagination(pageable, query);
//
//        List<Question> results = query.fetch();
//        long total = query.fetchCount();
//        return new PageImpl<>(results, pageable, total);
//    }

    @Override
    public PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO) {
        QQuestion question = QQuestion.question;

        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<Question> query = from(question);
        query.leftJoin(question.attachFiles, attachFile).on(attachFile.ord.eq(0));

//        query.where(question.delFlag.isFalse());
        query.orderBy(question.qno.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<QuestionListDTO> results =
                query.select(
                        Projections.bean(
                                QuestionListDTO.class,
                                question.qno,
                                question.title,
                                question.contents,
                                question.answered,
                                attachFile.fileName.as("fileName")
                        )
                );

        List<QuestionListDTO> dtoList = results.fetch();

        long total = query.fetchCount();

        return PageResponseDTO.<QuestionListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Page<Question> searchByTitleAndContents(String keyword, Pageable pageable) {
        QQuestion question = QQuestion.question;
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(question.title.containsIgnoreCase(keyword))
                .or(question.contents.containsIgnoreCase(keyword));

        JPQLQuery<Question> query = from(question).where(builder);
        getQuerydsl().applyPagination(pageable, query);
        List<Question> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Question> searchByWriter(String writer, Pageable pageable) {
        QQuestion question = QQuestion.question;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(question.writer.containsIgnoreCase(writer));

        JPQLQuery<Question> query = from(question).where(builder);
        getQuerydsl().applyPagination(pageable, query);
        List<Question> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}




