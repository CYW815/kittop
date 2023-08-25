package com.kittop.mapper;

import com.kittop.domain.OrderListVO;
import com.kittop.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderListMapper {

    // 주문내역 등록
    void saveOrderList(OrderListVO orderListVO);

    // 주문내역 업데이트(구매확정, 교환신청, 환불신청, 배송중, 배송완료 등)
    void updateOrderList(OrderListVO orderListVO);

    // 주문날짜 기준 일주일이 지나면 구매확정 (배송완료시)
    void updateOrderListByDate();

    // 주문내역 유저이메일 기준으로 가져오기
    List<OrderListVO> findOrderListByUserEmailPageRequestDTO(PageRequestDTO pageRequestDTO);

    void deleteOrderListByOrderId(long orderId);

    //board, review작성시 orderId 값으로 boardId, reviewId컬럼 update
    void updateOrderListByOrderId(OrderListVO orderListVO);

    //board, review에서 orderId값으로 order찾기
    OrderListVO findOrderListByOrderId(long orderId);
    int findOrderListCountByUserEmailPageRequestDTO(PageRequestDTO pageRequestDTO);

    //날짜로 주문내역 찾기
    List<OrderListVO> findOrderListByPeriod(PageRequestDTO pageRequestDTO);

    //주문상태에 따른 갯수 찾기
    int findOrderListCountByStatusPageRequestDTO(PageRequestDTO pageRequestDTO);

    // 전체 주문내역 가져오기, 검색값으로 주문내역 가져오기
    List<OrderListVO> findOrderListByPageRequestDTO(PageRequestDTO pageRequestDTO);

    // 전체 주문내역 개수 가져오기, 검색값으로 인한 주문내역 개수 가져오기
    int findOrderListCountByPageRequestDTO(PageRequestDTO pageRequestDTO);

    int findOrderListCountByPeriod(PageRequestDTO pageRequestDTO);

    int statusCount(String status);

    String statusTotal(String status);
}
