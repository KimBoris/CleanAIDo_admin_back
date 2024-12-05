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

    public CategorySearchImpl() {super(Category.class);}

    @Override
    public List<CategoryListDTO> listParents() {
        QCategory category = QCategory.category;

        JPQLQuery<Category> query = from(category)
                .where(category.depth.eq(1));

        JPQLQuery<CategoryListDTO> results = query.select(
                Projections.bean(
                        CategoryListDTO.class,
                        category.cno,
                        category.cname,
                        category.parent
                )
        );
        return results.fetch();
    }

    @Override
    public List<CategoryListDTO> listChildren(Long cno) {
        QCategory category = QCategory.category;

        JPQLQuery<Category> query = from(category)
                .where(category.parent.eq(cno));

        JPQLQuery<CategoryListDTO> results = query.select(
                Projections.bean(
                        CategoryListDTO.class,
                        category.cno,
                        category.cname,
                        category.parent
                )
        );
        return results.fetch();
    }
}
