package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.controller.exceptions.OrderNotFoundException;
import com.kodilla.ecommercee.controller.exceptions.UserNotFoundException;
import com.kodilla.ecommercee.domain.Order;
import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.dto.NewOrderDto;
import com.kodilla.ecommercee.domain.dto.OrderDto;
import com.kodilla.ecommercee.mapper.OrderMapper;
import com.kodilla.ecommercee.service.OrderService;
import com.kodilla.ecommercee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final UserService userService;

    @GetMapping(value = "{userId}")
    public ResponseEntity<List<OrderDto>>  getOrders(@PathVariable Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        List<Order> orders = user.getOrders();
        return ResponseEntity.ok(orderMapper.mapToOrdersDto(orders));
    }

    @GetMapping(value = "{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) throws OrderNotFoundException {
        Order order = orderService.findById(orderId);
        return ResponseEntity.ok(orderMapper.mapToOrderDto(order));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrder(@RequestBody NewOrderDto newOrderDto) throws UserNotFoundException {
        Order order = orderMapper.mapToNewOrder(newOrderDto);
        orderService.saveOrder(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) throws OrderNotFoundException, UserNotFoundException {
        Order order = orderService.findById(orderDto.getOrderId());
        order.setOrderCreated(orderDto.getOrderIssued());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setOrderedProducts(orderMapper.findAllById(orderDto.getProductsId()));
        order.setUser(userService.findById(orderDto.getUserId()));
        Order updatedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(orderMapper.mapToOrderDto(updatedOrder));
    }

    @DeleteMapping(value = "{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }
}