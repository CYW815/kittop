package com.kittop.service;

import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;

public interface OrderListService {

    void saveOrder(OrderListDTO orderListDTO);

    OrderListDTO findOrderListByOrderId(long orderId);

    void updateOrderList(OrderListDTO orderListDTO);

    void updateOrderListByOrderId(OrderListDTO orderListDTO);



    PageResponseDTO<OrderListDTO> findOrderListByUserEmailPageRequestDTO(PageRequestDTO pageRequestDTO);

    PageResponseDTO<OrderListDTO> findOrderListByPageRequestDTO(PageRequestDTO pageRequestDTO);

    PageResponseDTO<OrderListDTO>findOrderListByPeriod(PageRequestDTO pageRequestDTO);

    void deleteOrderListByOrderId(long orderId);

}





