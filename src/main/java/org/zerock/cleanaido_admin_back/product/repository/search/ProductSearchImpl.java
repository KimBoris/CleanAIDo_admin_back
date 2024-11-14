package org.zerock.cleanaido_admin_back.product.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.category.entity.QCategory;
import org.zerock.cleanaido_admin_back.product.entity.QProduct;


import java.util.List;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {super(Product.class);}

    @Override
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product).orderBy(product.pno.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<ProductListDTO> results =
                query.select(
                        Projections.bean(
                                ProductListDTO.class,
                                product.pno,
                                product.pcode,
                                product.pname,
                                product.price,
                                product.quantity,
                                product.pstatus,
                                product.createdAt,
                                product.updatedAt,
                                product.sellerId
                        )
                );

        List<ProductListDTO> dtoList = results.fetch();
        long total = query.fetchCount();
        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
    @Override
    public Page<Product> searchBy(String type, String keyword, Pageable pageable) {
        QProduct product = QProduct.product;
        log.info("---------------------");
        log.info("search start.");

        BooleanBuilder builder = new BooleanBuilder();
        if("pcode".equals(type)) {
            builder.and(product.pcode.containsIgnoreCase(keyword));
        }else if("pname".equals(type)) {
            builder.and(product.pname.containsIgnoreCase(keyword));
        }

        JPQLQuery<Product> query = from(product).where(builder);
        getQuerydsl().applyPagination(pageable, query);
        List<Product> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<CategoryDTO> searchCategoryBy(String keyword) {

        QCategory c1 = QCategory.category; // 자식 카테고리
        QCategory c2 = new QCategory("c2"); // 부모 카테고리 (별칭)

        // 쿼리 생성: 부모-자식 카테고리 조인
        JPQLQuery<Tuple> query = from(c1)
                .leftJoin(c2) // 자식 카테고리(c1)와 부모 카테고리(c2)를 조인
                .on(c1.parent.eq(c2.cno)) // c1.parent가 c2.cno와 같다는 조건
                .where(
                        (c1.depth.eq(1).and(c1.cname.like("%" + keyword + "%"))) // 깊이가 1인 카테고리에서 키워드 포함된 카테고리 찾기
                                .or(c1.depth.eq(2).and(
                                        c1.cname.like("%" + keyword + "%") // 깊이가 2인 카테고리에서 키워드 포함된 카테고리 찾기
                                                .or(c2.cname.like("%" + keyword + "%")) // 부모 카테고리에서 키워드 포함된 것 찾기
                                ))
                )
                .select(c1.cno, c1.cname, c2.cname.coalesce("")); // 카테고리 번호, 이름, 부모 카테고리 이름 선택 (COALESCE 사용)

        // CategoryDTO 객체로 변환
        JPQLQuery<CategoryDTO> resultsQuery = query.select(Projections.constructor(
                CategoryDTO.class,
                c1.cno,
                c1.cname,
                c2.cname.coalesce("") // parentName 필드에 매핑
        ));

        return resultsQuery.fetch(); // 결과 반환
    }


}
