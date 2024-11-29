package org.zerock.cleanaido_admin_back.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.fcm.dto.FCMRequestDTO;
import org.zerock.cleanaido_admin_back.fcm.service.FCMService;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FCMService fcmService;

    public PageResponseDTO<OrderListDTO> listOrders(PageRequestDTO pageRequestDTO, List<String> statuses) {
        Page<OrderListDTO> resultPage;
        var pageable = pageRequestDTO.toPageable();
        var searchDTO = pageRequestDTO.getSearchDTO();

        if (searchDTO == null || searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            resultPage = orderRepository.list(statuses, pageable);
        } else {
            switch (searchDTO.getSearchType()) {
                case "orderNumber":
                    resultPage = orderRepository.searchByOrderNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "productNumber":
                    resultPage = orderRepository.searchByProductNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "customerId":
                    resultPage = orderRepository.searchByCustomerId(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "phoneNumber":
                    resultPage = orderRepository.searchByPhoneNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "trackingNumber":
                    resultPage = orderRepository.searchByTrackingNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                default:
                    resultPage = Page.empty(pageable);
                    break;
            }
        }

        return new PageResponseDTO<>(resultPage.getContent(), pageRequestDTO, resultPage.getTotalElements());
    }
}
