package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.dao.CartRepository;
import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    public Cart findCartByUserId(int user_id) {
        Cart cart = cartRepository.findIdByUserId(user_id);
        System.out.println("cart id " + cart);
        //ArrayList<CartItem> cartItems =    }
        return cart;
    }

    public void saveCartItem(CartItem ci) {
        cartItemRepository.save(ci); 
    } 
}