package com.cmpe275.CartShare.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.OrderItemsRepository;
import com.cmpe275.CartShare.model.OrderItems;

@Service
public class OrderItemsService {

	@Autowired
	OrderItemsRepository orderItemsRepository;
	
	@Transactional
	public OrderItems save(OrderItems orderItem) {
		return orderItemsRepository.save(orderItem);
	}
}