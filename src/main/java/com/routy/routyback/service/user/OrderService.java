package com.routy.routyback.service.user;

import com.routy.routyback.dto.order.OrderDetailResponse;
import com.routy.routyback.dto.order.OrderListItemResponse;
import com.routy.routyback.dto.order.OrderStatusSummaryResponse;
import com.routy.routyback.mapper.order.OrderMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 주문 서비스 구현체
 * 주문 상태 요약, 주문 목록, 주문 상세 조회 기능을 제공합니다.
 * @author 김지용
 */
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderMapper orderMapper;

    /**
     * 주문 상태 요약 조회
     * 회원의 주문 상태(결제 완료, 준비중, 배송중, 배송완료 등)를 집계합니다.
     * @param userId 회원 번호
     * @return 주문 상태 요약 응답 DTO
     */
    @Override
    public OrderStatusSummaryResponse getSummary(String userId) {
        return orderMapper.getOrderStatusSummary(userId);
    }

    /**
     * 주문 목록 조회
     * 회원이 가진 전체 주문 목록을 최신순으로 조회합니다.
     * @param userId 회원 번호
     * @return 주문 목록 응답 DTO 리스트
     */
    @Override
    public List<OrderListItemResponse> getList(String userId) {
        return orderMapper.getOrderList(userId);
    }

    /**
     * 주문 상세 조회
     * 단일 주문에 대한 기본 정보와 주문 상품 목록을 함께 조회합니다.
     * @param userId 회원 번호
     * @param odNo 주문 번호
     * @return 주문 상세 응답 DTO
     */
    @Override
    public OrderDetailResponse getDetail(String userId, Long odNo) {
        // 기본 상세 정보 조회
        OrderDetailResponse detail = orderMapper.getOrderDetail(userId, odNo);

        // 주문이 존재하지 않거나 사용자 소유가 아닌 경우 방어 처리
        if (detail == null) {
            throw new IllegalArgumentException("해당 주문을 조회할 수 없습니다. (userId 또는 odNo 불일치)");
        }

        // 주문 상품 목록 조회
        detail.setItems(orderMapper.getOrderItems(odNo));

        return detail;
    }
}