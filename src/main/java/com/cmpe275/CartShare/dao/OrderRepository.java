package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

}