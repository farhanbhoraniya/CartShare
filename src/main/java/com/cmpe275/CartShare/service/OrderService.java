package com.cmpe275.CartShare.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.OrderRepository;
import com.cmpe275.CartShare.model.Order;

import com.cmpe275.CartShare.dao.OrderRepository;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


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
		List<Order> orders= orderRepository.findByBuyer(user);
		return orders;
	}

	public List<Order> getOpenOrdersByUserId(int userid){
		return orderRepository.findByBuyerIdAndSelfpick(userid);
	}

}
