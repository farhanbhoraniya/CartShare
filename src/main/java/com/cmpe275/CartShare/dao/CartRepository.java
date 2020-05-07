package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    public int findIdByUserId(int userid);
}
