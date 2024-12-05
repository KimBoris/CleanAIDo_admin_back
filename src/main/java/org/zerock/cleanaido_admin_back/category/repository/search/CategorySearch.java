package org.zerock.cleanaido_admin_back.category.repository.search;

import org.zerock.cleanaido_admin_back.category.dto.CategoryListDTO;

import java.util.List;

public interface CategorySearch {

    List<CategoryListDTO> listParents();

    List<CategoryListDTO> listChildren(Long cno);
}
