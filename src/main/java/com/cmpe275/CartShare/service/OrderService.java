package com.cmpe275.CartShare.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.OrderRepository;
import com.cmpe275.CartShare.model.Order;

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
}
