package org.zerock.cleanaido_admin_back.user.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.entity.QUser;
import org.zerock.cleanaido_admin_back.user.entity.User;

import java.util.List;

public class UserSearchImpl extends QuerydslRepositorySupport implements UserSearch {

    public UserSearchImpl() {
        super(User.class);
    }

    @Override
    public PageResponseDTO<UserListDTO> list(PageRequestDTO pageRequestDTO) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user).orderBy(user.userId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<UserListDTO> results =
                query.select(
                        Projections.bean(
                                UserListDTO.class,
                                user.userId,
                                user.password
//                                user.businessName,
//                                user.businessType,
//                                user.ownerName,
//                                user.businessAddress,
//                                user.businessStatus,
//                                user.businessCategory,
//                                user.storeName,
//                                user.commerceLicenseNum,
//                                user.businessLicenseFile,
//                                user.originAddress,
//                                user.contactNumber,
//                                user.accountNumber,
//                                user.userStatus,
//                                user.delFlag,
//                                user.adminRole,
//                                user.createDate


                        )

                );
        List<UserListDTO> dtoList = results.fetch();

        long total = query.fetchCount();
        return PageResponseDTO.<UserListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();


    }
}
