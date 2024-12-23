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

import java.util.List;

public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {

    // 부모 클래스 QuerydslRepositorySupport의 생성자를 호출하여 Review 엔티티를 기반으로 동작하도록 설정
    public ReviewSearchImpl() {
        super(Review.class);
    }

    /**
     * 리뷰 리스트를 페이징하여 가져오는 메서드
     *
     * @param pageRequestDTO 페이징 및 검색 조건을 담고 있는 DTO
     * @return 페이징 처리된 리뷰 리스트 및 메타 데이터를 포함한 PageResponseDTO
     */
    @Override
    public PageResponseDTO<ReviewListDTO> list(PageRequestDTO pageRequestDTO) {

        // QueryDSL에서 사용할 엔티티 객체 정의
        QReview review = QReview.review; // 리뷰 엔티티
        QProduct product = QProduct.product; // 제품 엔티티
        QCustomer customer = QCustomer.customer; // 고객 엔티티
        QReviewImage reviewImages = QReviewImage.reviewImage; // 리뷰 이미지 엔티티

        // JPQLQuery 객체를 통해 쿼리 생성
        JPQLQuery<Review> query = from(review)
                // 리뷰와 제품 조인
                .leftJoin(review.product, product).on(review.product.pno.eq(product.pno))
                // 리뷰와 고객 조인
                .leftJoin(review.customer, customer).on(review.customer.customerId.eq(customer.customerId))
                // 리뷰와 리뷰 이미지 조인
                .leftJoin(review.reviewImages, reviewImages)
                // 리뷰 번호 기준 내림차순 정렬
                .orderBy(review.reviewNumber.desc());

        // 페이지 요청 정보를 기반으로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        // QueryDSL을 사용하여 페이징 적용
        getQuerydsl().applyPagination(pageable, query);

        // 결과 매핑: 쿼리 결과를 ReviewListDTO에 매핑
        JPQLQuery<ReviewListDTO> results = query.select(
                Projections.bean(
                        ReviewListDTO.class,
                        review.reviewNumber, // 리뷰 번호
                        review.reviewContent, // 리뷰 내용
                        review.createDate, // 리뷰 작성일
                        review.score, // 리뷰 점수
                        product.pname.as("productName"), // 제품 이름
                        customer.customerName.as("customerName"), // 고객 이름
                        customer.customerId.as("customerId") // 고객 ID
                )
        );

        // 쿼리 실행 및 결과 리스트로 변환
        List<ReviewListDTO> dtoList = results.fetch();
        // 총 리뷰 개수 계산
        long total = results.fetchCount();

        // PageResponseDTO를 생성하여 결과 반환
        return PageResponseDTO.<ReviewListDTO>withAll()
                .dtoList(dtoList) // 리뷰 데이터 리스트
                .totalCount(total) // 총 리뷰 개수
                .pageRequestDTO(pageRequestDTO) // 요청 페이징 정보
                .build();
    }
}
