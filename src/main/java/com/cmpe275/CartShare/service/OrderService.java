package com.cmpe275.CartShare.service;


import com.cmpe275.CartShare.dao.OrderRepository;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Transactional
    public Order save(Order order) {

        Order newOrder = orderRepository.save(order);
//		orderRepository.flush();
        return newOrder;
    }

    public List<Order> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByBuyer(user);
        return orders;
    }

    public List<Order> getOpenOrdersByUserId(int userid) {
        return orderRepository.findByBuyerIdAndSelfpick(userid);
    }

    public List<Order> getOrdersByPool(Pool pool) {
        return orderRepository.findByPool(pool);
    }

    public Order getOrderByOrderId(int orderId)
    {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrdersPendingDelivery(User user)
    {
        return orderRepository.findOrderByStatusAndPickedby(Order.ORDER_PICKED_UP, user);
    }

}
