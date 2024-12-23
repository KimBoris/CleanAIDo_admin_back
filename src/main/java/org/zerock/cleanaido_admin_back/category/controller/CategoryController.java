package org.zerock.cleanaido_admin_back.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.category.dto.CategoryListDTO;
import org.zerock.cleanaido_admin_back.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    //1차 카테고리 목록 가져오기
    @GetMapping("list")
    public ResponseEntity<List<CategoryListDTO>> list() {

        return ResponseEntity.ok(categoryService.listCategoryParents());
    }

    //2차 카테고리 목록 가져오기
    @GetMapping("children/{cno}")
    public ResponseEntity<List<CategoryListDTO>> children(
            @PathVariable Long cno
    ) {

        return ResponseEntity.ok(categoryService.listCategoryChildren(cno));
    }

    //2차 카테고리 생성
    @PostMapping(value = "children")
    public ResponseEntity<Long> addChild(
            @RequestParam("parent") Long parentCno,
            @RequestParam("cname") String cname
    ){
        return ResponseEntity.ok(categoryService.addChildCategory(parentCno, cname));
    }

    //1차 카테고리 생성
    @PostMapping(value = "parent")
    public ResponseEntity<Long> addParent(
            @RequestParam("cname") String cname
    ){
        return ResponseEntity.ok(categoryService.addParentCategory(cname));
    }

    //카테고리 삭제
    @DeleteMapping(value = "{cno}")
    public ResponseEntity<Long> delete(
            @PathVariable Long cno
    ){
        return ResponseEntity.ok(categoryService.deleteCategory(cno));
    }
}
