package org.zerock.cleanaido_admin_back.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
import org.zerock.cleanaido_admin_back.category.dto.CategoryListDTO;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.category.repository.CategoryRepository;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //1차 카테고리 목록
    public List<CategoryListDTO> listCategoryParents() {
        return categoryRepository.listParents();
    }

    //2차 카테고리 목록
    public List<CategoryListDTO> listCategoryChildren(Long cno) {
        return categoryRepository.listChildren(cno);
    }

    //2차 카테고리 생성
    public Long addChildCategory(Long parentCno, String cname) {
        Category category = Category.builder()
                .cname(cname)
                .parent(parentCno)
                .depth(2)
                .build();
        categoryRepository.save(category);

        return category.getCno();
    }

    //1차 카테고리 생성
    public Long addParentCategory(String cname) {
        Category category = Category.builder()
                .cname(cname)
                .depth(1)
                .build();
        categoryRepository.save(category);

        return category.getCno();
    }

    //카테고리 삭제
    public Long deleteCategory(Long cno){
        categoryRepository.deleteById(cno);
        return cno;
    }
}
