package com.cmpe275.CartShare.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Product;

@Service
public class CartItemService {

    @Autowired
    CartItemRepository cartItemRepostiory;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void delete(CartItem ci) {
        Query query = entityManager.createQuery(
                "DELETE from CartItem"
                        + " WHERE cart = :cartid"
                        + " and sku = :sku"
                        + " and storeid = :sid");
        query.setParameter("cartid", ci.getCart());
        query.setParameter("sku", ci.getProduct().getSku());
        query.setParameter("sid", ci.getProduct().getStoreid());
        query.executeUpdate();
    }
    
    @Transactional
    public Object saveOrUpdateCartItem(CartItem ci) {
        CartItem cartResult = findCartItem(ci.getProduct(), ci.getCart());
        
        if(cartResult == null) {
            return cartItemRepostiory.save(ci);
        }
        
        Query query = entityManager.createQuery(
                "UPDATE CartItem SET quantity = :qty, price = :p"
                + " WHERE cart = :cartid"
                + " and sku = :sku"
                + " and storeid = :sid");
        
        query.setParameter("qty", ci.getQuantity());
        query.setParameter("p", ci.getPrice());
        query.setParameter("cartid", ci.getCart());
        query.setParameter("sku", ci.getProduct().getSku());
        query.setParameter("sid", ci.getProduct().getStoreid());
        
        return query.executeUpdate();          
    }

    public CartItem findCartItem(Product product, Cart cart) {
        Query queryToFindCart = entityManager.createQuery("from CartItem "
                + " WHERE cart = :cartid"
                + " and sku = :sku"
                + " and storeid = :sid");
        queryToFindCart.setParameter("cartid", cart);
        queryToFindCart.setParameter("sku", product.getSku());
        queryToFindCart.setParameter("sid", product.getStoreid());
        try {
            return (CartItem) queryToFindCart.getSingleResult();
        }catch(Exception e) {
            return null;
        }
    }
}
