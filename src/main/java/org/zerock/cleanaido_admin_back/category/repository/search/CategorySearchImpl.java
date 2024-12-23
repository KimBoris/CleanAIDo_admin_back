package org.zerock.cleanaido_admin_back.category.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.category.dto.CategoryListDTO;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.category.entity.QCategory;

import java.util.List;

@Log4j2
public class CategorySearchImpl extends QuerydslRepositorySupport implements CategorySearch {

    // 기본 생성자: Category 클래스와 QuerydslRepositorySupport를 연결
    public CategorySearchImpl() {
        super(Category.class);
    }

    // 부모 카테고리 리스트를 반환하는 메서드
    @Override
    public List<CategoryListDTO> listParents() {
        QCategory category = QCategory.category;

        // JPQLQuery를 생성하여 depth가 1인 카테고리를 조회 (depth 1은 부모 카테고리를 의미)
        JPQLQuery<Category> query = from(category)
                .where(category.depth.eq(1)); // depth가 1인 조건 추가

        // 조회된 카테고리 데이터를 CategoryListDTO로 변환
        JPQLQuery<CategoryListDTO> results = query.select(
                Projections.bean(
                        CategoryListDTO.class,
                        category.cno,
                        category.cname,
                        category.parent
                )
        );
        return results.fetch(); // 변환된 결과 리스트 반환
    }

    //2차 카테고리 목록
    @Override
    public List<CategoryListDTO> listChildren(Long cno) {
        QCategory category = QCategory.category;

        // JPQLQuery를 생성하여 parent가 cno인 카테고리를 조회 (parent는 부모 카테고리 번호)
        JPQLQuery<Category> query = from(category)
                .where(category.parent.eq(cno));

        // 조회된 카테고리 데이터를 CategoryListDTO로 변환
        JPQLQuery<CategoryListDTO> results = query.select(
                Projections.bean(
                        CategoryListDTO.class,
                        category.cno,
                        category.cname,
                        category.parent
                )
        );
        return results.fetch(); // 변환된 결과 리스트 반환
    }
}
