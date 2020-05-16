package com.cmpe275.CartShare.dao;

import java.util.List;

import org.hibernate.sql.Insert;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer>{
	public OrderItems save(OrderItems orderItem);
	public OrderItems saveAndFlush(OrderItems orderItem);
	public List<OrderItems> findByOrder(Order order);
	public List<OrderItems> findOrderItemsByOrderId(int order_id);
    

}
