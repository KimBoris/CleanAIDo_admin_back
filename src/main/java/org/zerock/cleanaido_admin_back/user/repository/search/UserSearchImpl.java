package org.zerock.cleanaido_admin_back.user.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.entity.QUser;
import org.zerock.cleanaido_admin_back.user.entity.User;

import java.util.List;


@Log4j2
public class UserSearchImpl extends QuerydslRepositorySupport implements UserSearch {

    public UserSearchImpl() {
        super(User.class);
    }

    @Override
    public PageResponseDTO<UserListDTO> list(PageRequestDTO pageRequestDTO) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user).where(user.adminRole.isFalse()).
                where(user.userStatus.eq("입점").or(user.userStatus.eq("활동정지")))
                .orderBy(user.userId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<UserListDTO> results =
                query.select(
                        Projections.bean(
                                UserListDTO.class,
                                user.userId,
                                user.password,
                                user.businessName,
                                user.businessType,
                                user.ownerName,
                                user.businessAddress,
                                user.businessStatus,
                                user.businessCategory,
                                user.storeName,
                                user.commerceLicenseNum,
                                user.businessLicenseFile,
                                user.originAddress,
                                user.contactNumber,
                                user.accountNumber,
                                user.userStatus,
                                user.delFlag,
                                user.adminRole,
                                user.createDate
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

    @Override
    public PageResponseDTO<UserListDTO> searchBy(String type, String keyword, PageRequestDTO pageRequestDTO) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user);

        log.info("=============================================");
        log.info("=============================================");

        log.info(type);
        log.info(keyword);

        log.info("=============================================");

        log.info("=============================================");


        //type = 스토어명, 유저 아이디, 사업자명
        if (type == null || type.isEmpty()) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.or(user.ownerName.like("%" + keyword + "%"))
                    .or(user.userId.like("%" + keyword + "%"))
                    .or(user.storeName.like("%" + keyword + "%"));
            query.where(builder).distinct();
        } else {
            BooleanBuilder builder = new BooleanBuilder();
            if (type.equals("userId")) {
                builder.or(user.userId.like("%" + keyword + "%"));
            } else if (type.equals("ownerName")) {
                builder.or(user.ownerName.like("%" + keyword + "%"));
            } else if (type.equals("storeName")) {
                builder.or(user.storeName.like("%" + keyword + "%"));
            } else if (type.equals("UserStatus")) {
                builder.or(user.userStatus.like("%" + keyword + "%"));
            }
            query.where(builder);
        }

        query.orderBy(user.userId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<UserListDTO> results =
                query.select(
                        Projections.bean(
                                UserListDTO.class,
                                user.userId,
                                user.password,
                                user.businessName,
                                user.businessType,
                                user.ownerName,
                                user.businessAddress,
                                user.businessStatus,
                                user.businessCategory,
                                user.storeName,
                                user.commerceLicenseNum,
                                user.businessLicenseFile,
                                user.originAddress,
                                user.contactNumber,
                                user.accountNumber,
                                user.userStatus,
                                user.delFlag,
                                user.adminRole,
                                user.createDate

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

    @Override
    public UserReadDTO getUserById(String userId) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user).where(user.userId.eq(userId));
        User result = query.fetchOne();

        if (result == null) {
            throw new IllegalArgumentException("User not Fount");
        }
        return UserReadDTO.builder()
                .userId(result.getUserId())
                .password(result.getPassword())
                .businessNumber(result.getBusinessNumber())
                .businessName(result.getBusinessName())
                .businessType(result.getBusinessType())
                .ownerName(result.getOwnerName())
                .businessAddress(result.getBusinessAddress())
                .businessStatus(result.getBusinessStatus())
                .businessCategory(result.getBusinessCategory())
                .storeName(result.getStoreName())
                .commerceLicenseNum(result.getCommerceLicenseNum())
                .businessLicenseFile(result.getBusinessLicenseFile())
                .originAddress(result.getOriginAddress())
                .contactNumber(result.getContactNumber())
                .accountNumber(result.getAccountNumber())
                .userStatus(result.getUserStatus())
                .delFlag(result.isDelFlag())
                .adminRole(result.isAdminRole())
                .createDate(result.getCreateDate())
                .build();
    }

    @Override
    public PageResponseDTO<UserListDTO> getUserByStatus(PageRequestDTO pageRequestDTO) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user)
                .where(user.userStatus.eq("입점요청"))
                .orderBy(user.userId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<UserListDTO> results =
                query.select(
                        Projections.bean(
                                UserListDTO.class,
                                user.userId,
                                user.password,
                                user.businessName,
                                user.businessType,
                                user.ownerName,
                                user.businessAddress,
                                user.businessStatus,
                                user.businessCategory,
                                user.storeName,
                                user.commerceLicenseNum,
                                user.businessLicenseFile,
                                user.originAddress,
                                user.contactNumber,
                                user.accountNumber,
                                user.userStatus,
                                user.delFlag,
                                user.adminRole,
                                user.createDate
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
