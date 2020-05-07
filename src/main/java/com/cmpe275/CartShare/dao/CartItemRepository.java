package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCartId(Cart cartId);

    List<CartItem> findByCartId(int id);

}
