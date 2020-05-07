package com.cmpe275.CartShare.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.model.User;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	public Order findById(int id);
	public List<Order> findByBuyer(User Buyer);
	public Order save(Order order);
	public Order findByBuyerAndDate(User Buyer, Date date);

}
