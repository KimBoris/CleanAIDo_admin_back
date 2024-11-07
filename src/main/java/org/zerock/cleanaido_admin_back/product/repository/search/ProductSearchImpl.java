package org.zerock.cleanaido_admin_back.product.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.entity.Product;
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

}
