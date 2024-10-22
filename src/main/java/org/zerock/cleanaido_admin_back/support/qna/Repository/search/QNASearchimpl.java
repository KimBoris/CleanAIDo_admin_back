package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.support.qna.entity.QQuestion;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class QNASearchimpl extends QuerydslRepositorySupport implements QNASearch{

    public QNASearchimpl(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    public Page<Question> list(Pageable pageable) {

        log.info("-------------------list-----------");

        QQuestion question = QQuestion.question;

        JPQLQuery<Question> query = from(question);

        //페이징 처리 정렬처리
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery =
                query.select(
                        question.title,
                        question.contents
                );

        tupleQuery.fetch();




        return null;
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCno(Long cno, PageRequestDTO pageRequestDTO) {

        Pageable pageable =
                PageRequest.of(
                        pageRequestDTO.getPage() -1,
                        pageRequestDTO.getSize(),
                        Sort.by("pno").descending()
                );

        QProduct product = QProduct.product;
        QReview review = QReview.review;
        QAttachFile attachFile = QAttachFile.attachFile;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(review).on(review.product.eq(product));
        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
        query.leftJoin(product.attachFiles, attachFile);

        query.where(attachFile.ord.eq(0));
        query.where(categoryProduct.category.cno.eq(cno));
        query.groupBy(product);

        //페이징 처리 정렬처리
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery =
                query.select(
                        product,
                        review.count(),
                        attachFile.filename
                );

        List<Tuple> tupleList = tupleQuery.fetch();

        log.info(tupleList);

        if(tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = new ArrayList<>();

        tupleList.forEach(t -> {
            Product productObj = t.get(0, Product.class);
            long count = t.get(1, Long.class);
            String fileName = t.get(2, String.class);

            ProductListDTO dto = ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .name(productObj.getName())
                    .fileName(fileName)
                    .reviewCnt(count)
                    .tags(productObj.getTags().stream().toList())
                    .build();


            dtoList.add(dto);

        });

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
