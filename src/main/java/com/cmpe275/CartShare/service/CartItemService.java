package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.model.CartItem;

@Service
public class CartItemService {

	@Autowired
	CartItemRepository cartItemRepostiory;
	
	@Transactional
	public void delete(CartItem cartItems) {
		cartItemRepostiory.delete(cartItems);
	}
}
