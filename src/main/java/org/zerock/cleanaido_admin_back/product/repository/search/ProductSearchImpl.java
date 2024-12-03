//package org.zerock.cleanaido_admin_back.product.repository.search;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.JPQLQuery;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//import org.zerock.cleanaido_admin_back.category.entity.QProductCategory;
//import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
//import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
//import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
//import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
//import org.zerock.cleanaido_admin_back.product.dto.ProductReadDTO;
//import org.zerock.cleanaido_admin_back.product.entity.Product;
//import org.zerock.cleanaido_admin_back.category.entity.QCategory;
//import org.zerock.cleanaido_admin_back.product.entity.QImageFiles;
//import org.zerock.cleanaido_admin_back.product.entity.QProduct;
//import org.zerock.cleanaido_admin_back.user.entity.QUser;
//
//
//import java.util.List;
//
//@Log4j2
//public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
//
//    public ProductSearchImpl() {super(Product.class);}
//
//    @Override
//    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
//        QProduct product = QProduct.product;
//        QUser user = QUser.user; // User QEntity
//
//        // Product와 User를 조인
//        JPQLQuery<Product> query = from(product)
//                .join(product.seller, user)
//                .orderBy(product.pno.desc());
//
//        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
//        getQuerydsl().applyPagination(pageable, query);
//
//        JPQLQuery<ProductListDTO> results = query.select(
//                Projections.bean(
//                        ProductListDTO.class,
//                        product.pno,
//                        product.pcode,
//                        product.pname,
//                        product.price,
//                        product.quantity,
//                        product.pstatus,
//                        product.updatedAt,
//                        user.storeName.as("storeName") // 관리자 리스트에만 storeName 포함
//                )
//        );
//
//        List<ProductListDTO> dtoList = results.fetch();
//        long total = query.fetchCount();
//
//        return PageResponseDTO.<ProductListDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//    }
//
//
//    @Override
//    public PageResponseDTO<ProductListDTO> listBySeller(PageRequestDTO pageRequestDTO, String sellerId) {
//        QProduct product = QProduct.product;
//        QUser user = QUser.user; // User QEntity
//
//        JPQLQuery<Product> query = from(product)
//                .join(product.seller, user) // Product와 User를 조인
//                .where(user.userId.eq(sellerId)) // User ID 필터링
//                .orderBy(product.pno.desc());
//
//        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
//        getQuerydsl().applyPagination(pageable, query);
//
//        JPQLQuery<ProductListDTO> results = query.select(
//                Projections.bean(
//                        ProductListDTO.class,
//                        product.pno,
//                        product.pcode,
//                        product.pname,
//                        product.price,
//                        product.quantity,
//                        product.pstatus,
//                        product.createdAt,
//                        product.updatedAt,
//                        user.userId.as("sellerId") // DTO에 sellerId 포함
//                )
//        );
//
//        List<ProductListDTO> dtoList = results.fetch();
//        long total = query.fetchCount();
//
//        return PageResponseDTO.<ProductListDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//    }
//
//
//    @Override
//    public Page<Product> searchBy(String type, String keyword, Pageable pageable) {
//        QProduct product = QProduct.product;
//        log.info("---------------------");
//        log.info("search start.");
//
//        BooleanBuilder builder = new BooleanBuilder();
//        if("pcode".equals(type)) {
//            builder.and(product.pcode.containsIgnoreCase(keyword));
//        }else if("pname".equals(type)) {
//            builder.and(product.pname.containsIgnoreCase(keyword));
//        }
//
//        JPQLQuery<Product> query = from(product).where(builder);
//        getQuerydsl().applyPagination(pageable, query);
//        List<Product> results = query.fetch();
//        long total = query.fetchCount();
//
//        return new PageImpl<>(results, pageable, total);
//    }
//
//    @Override
//    public List<CategoryDTO> searchCategoryBy(String keyword) {
//
//        QCategory c1 = QCategory.category; // 자식 카테고리
//        QCategory c2 = new QCategory("c2"); // 부모 카테고리 (별칭)
//
//        // 쿼리 생성: 부모-자식 카테고리 조인
//        JPQLQuery<Tuple> query = from(c1)
//                .leftJoin(c2) // 자식 카테고리(c1)와 부모 카테고리(c2)를 조인
//                .on(c1.parent.eq(c2.cno)) // c1.parent가 c2.cno와 같다는 조건
//                .where(
//                        (c1.depth.eq(1).and(c1.cname.like("%" + keyword + "%"))) // 깊이가 1인 카테고리에서 키워드 포함된 카테고리 찾기
//                                .or(c1.depth.eq(2).and(
//                                        c1.cname.like("%" + keyword + "%") // 깊이가 2인 카테고리에서 키워드 포함된 카테고리 찾기
//                                                .or(c2.cname.like("%" + keyword + "%")) // 부모 카테고리에서 키워드 포함된 것 찾기
//                                ))
//                )
//                .select(c1.cno, c1.cname, c2.cname.coalesce("")); // 카테고리 번호, 이름, 부모 카테고리 이름 선택 (COALESCE 사용)
//
//        // CategoryDTO 객체로 변환
//        JPQLQuery<CategoryDTO> resultsQuery = query.select(Projections.constructor(
//                CategoryDTO.class,
//                c1.cno,
//                c1.cname,
//                c2.cname.coalesce("") // parentName 필드에 매핑
//        ));
//
//        return resultsQuery.fetch(); // 결과 반환
//    }
//
//    @Override
//    public ProductReadDTO getProduct(Long pno) {
//        QProduct product = QProduct.product;
//        QProductCategory productCategory = QProductCategory.productCategory;
//        QCategory category = QCategory.category;
//
//        // Fetch Product data
//        JPQLQuery<Product> productQuery = from(product).where(product.pno.eq(pno));
//        Product result = productQuery.fetchOne();
//
//        if (result == null) {
//            throw new IllegalArgumentException("Product not found with pno: " + pno);
//        }
//
//        // Fetch categories through ProductCategory
//        JPQLQuery<Long> categoryQuery = from(productCategory)
//                .join(productCategory.category, category)
//                .where(productCategory.product.pno.eq(pno))
//                .select(category.cno);
//        List<Long> categories = categoryQuery.fetch();
//
//// Split image files by type
//        List<String> imageFiles = result.getImageFiles().stream()
//                .filter(imageFile -> !imageFile.isType()) // `type`이 `false`인 경우
//                .map(org.zerock.cleanaido_admin_back.product.entity.ImageFiles::getFileName) // FQN 사용
//                .toList();
//
//        List<String> detailImageFiles = result.getImageFiles().stream()
//                .filter(org.zerock.cleanaido_admin_back.product.entity.ImageFiles::isType) // `type`이 `true`인 경우
//                .map(org.zerock.cleanaido_admin_back.product.entity.ImageFiles::getFileName) // FQN 사용
//                .toList();
//
//        // Build DTO
//        return ProductReadDTO.builder()
//                .pno(result.getPno())
//                .pcode(result.getPcode())
//                .pname(result.getPname())
//                .price(result.getPrice())
//                .quantity(result.getQuantity())
//                .createdAt(result.getCreatedAt())
//                .updatedAt(result.getUpdatedAt())
//                .imageFiles(imageFiles)
//                .detailImageFiles(detailImageFiles)
//                .usageImageFiles(result.getUsageImageFiles().stream()
//                        .map(org.zerock.cleanaido_admin_back.product.entity.UsageImageFile::getFileName) // FQN 사용
//                        .toList())
//                .categories(categories) // ProductCategory를 통해 가져온 카테고리 번호 리스트
//                .tags(result.getPtags())
//                .pstatus(result.getPstatus())
//                .build();
//    }
//
//}
