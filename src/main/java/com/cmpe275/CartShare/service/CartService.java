package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.dao.CartRepository;
import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

//    public int findCartByUserId(int user_id) {
//        int cart = cartRepository.findCartByUserId(user_id);
//        System.out.println("cart id " + cart);
//        //ArrayList<CartItem> cartItems =    }
//        return cart;
   // }

    public CartItem saveCartItem(CartItem ci) {
        return cartItemRepository.save(ci); 
    }

    public Cart findIdByUserId(int user_id) {
        Cart cart = cartRepository.findCartByUserId(user_id);
        System.out.println("cart id " + cart);
        //ArrayList<CartItem> cartItems =    }
        return cart;
    }

    public List<CartItem> findByCartId(Cart cartId) {
        String hql = "from CartItem where cart=:cartId";
        Query q = entityManager.createQuery(hql);
        q.setParameter("cartId", cartId);
        
        return q.getResultList();
    }
}