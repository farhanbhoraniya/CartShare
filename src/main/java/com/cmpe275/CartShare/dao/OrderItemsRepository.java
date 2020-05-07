package com.cmpe275.CartShare.dao;

import org.hibernate.sql.Insert;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer>{
	public OrderItems save(OrderItems orderItem);
	public OrderItems saveAndFlush(OrderItems orderItem);

}
