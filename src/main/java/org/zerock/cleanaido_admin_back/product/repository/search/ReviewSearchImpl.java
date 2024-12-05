package org.zerock.cleanaido_admin_back.product.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.entity.QCustomer;
import org.zerock.cleanaido_admin_back.product.dto.ReviewListDTO;
import org.zerock.cleanaido_admin_back.product.entity.QProduct;
import org.zerock.cleanaido_admin_back.product.entity.QReview;
import org.zerock.cleanaido_admin_back.product.entity.QReviewImage;
import org.zerock.cleanaido_admin_back.product.entity.Review;
import org.zerock.cleanaido_admin_back.user.entity.QUser;

import java.util.List;

public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {

    public ReviewSearchImpl() {

        super(Review.class);
    }

    @Override
    public PageResponseDTO<ReviewListDTO> list(PageRequestDTO pageRequestDTO) {

        QReview review = QReview.review;
        QProduct product = QProduct.product;
        QCustomer customer = QCustomer.customer;
        QReviewImage reviewImages = QReviewImage.reviewImage;

        JPQLQuery<Review> query = from(review)
                .leftJoin(review.product, product).on(review.product.pno.eq(product.pno))
                .leftJoin(review.customer, customer).on(review.customer.customerId.eq(customer.customerId))
                .leftJoin(review.reviewImages, reviewImages)
                .orderBy(review.reviewNumber.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<ReviewListDTO> results = query.select(
                Projections.bean(
                        ReviewListDTO.class,
                        review.reviewNumber,
                        review.reviewContent,
                        review.createDate,
                        review.score,
                        product.pname.as("productName"),
                        customer.customerName.as("customerName"),
                        customer.customerId.as("customerId"),
                        product.pname.as("productName")

                )
        );


        List<ReviewListDTO> dtoList = results.fetch();
        long total = results.fetchCount();

        return PageResponseDTO.<ReviewListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
