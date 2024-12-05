package org.zerock.cleanaido_admin_back.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.category.repository.search.CategorySearch;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategorySearch {

}
