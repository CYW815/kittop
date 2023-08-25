package com.kittop.service;

import com.kittop.domain.BoardVO;
import com.kittop.domain.OrderListVO;
import com.kittop.dto.BoardDTO;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.mapper.OrderListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderListServiceImpl implements OrderListService {

    private final OrderListMapper orderListMapper;
    private final ModelMapper modelMapper;
    @Override
    public void saveOrder(OrderListDTO orderListDTO) {
        orderListMapper.saveOrderList(modelMapper.map(orderListDTO, OrderListVO.class));
    }

    @Override
    public OrderListDTO findOrderListByOrderId(long orderId) {
        return modelMapper.map(orderListMapper.findOrderListByOrderId(orderId), OrderListDTO.class);
    }


    @Override
    public PageResponseDTO<OrderListDTO> findOrderListByUserEmailPageRequestDTO(PageRequestDTO pageRequestDTO) {
        List<OrderListDTO> dtoList = orderListMapper.findOrderListByUserEmailPageRequestDTO(pageRequestDTO).stream().map(o -> modelMapper.map(o, OrderListDTO.class)).collect(Collectors.toList());
        int total = orderListMapper.findOrderListCountByUserEmailPageRequestDTO(pageRequestDTO);
        PageResponseDTO<OrderListDTO> pageResponseDTO = PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public PageResponseDTO<OrderListDTO> findOrderListByPageRequestDTO(PageRequestDTO pageRequestDTO) {
        List<OrderListDTO> dtoList = orderListMapper.findOrderListByPageRequestDTO(pageRequestDTO).stream().map(o -> modelMapper.map(o, OrderListDTO.class)).collect(Collectors.toList());
        int total = orderListMapper.findOrderListCountByPageRequestDTO(pageRequestDTO);
        PageResponseDTO<OrderListDTO> pageResponseDTO = PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public PageResponseDTO<OrderListDTO> findOrderListByPeriod(PageRequestDTO pageRequestDTO) {
        List<OrderListDTO> dtoList = orderListMapper.findOrderListByPeriod(pageRequestDTO).stream().map(o -> modelMapper.map(o, OrderListDTO.class)).collect(Collectors.toList());
        int total = orderListMapper.findOrderListCountByPeriod(pageRequestDTO);
        PageResponseDTO<OrderListDTO> pageResponseDTO = PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public void updateOrderList(OrderListDTO orderListDTO) {
        orderListMapper.updateOrderList(modelMapper.map(orderListDTO, OrderListVO.class));
    }

    @Override
    public void deleteOrderListByOrderId(long orderId) {
        orderListMapper.deleteOrderListByOrderId(orderId);
    }

    @Override
    public void updateOrderListByOrderId(OrderListDTO orderListDTO) {
        orderListMapper.updateOrderListByOrderId(modelMapper.map(orderListDTO, OrderListVO.class));
    }

}
