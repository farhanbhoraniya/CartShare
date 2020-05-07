package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.dao.CartRepository;
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

    public List<CartItem> findCartByUserId(int user_id) {
        int cartid = cartRepository.findIdByUserId(user_id);
        System.out.println("cart id " + cartid);
        //ArrayList<CartItem> cartItems =    }
        return null;

    }
}