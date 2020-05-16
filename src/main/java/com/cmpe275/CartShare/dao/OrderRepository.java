package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    public Order findById(int id);

    public List<Order> findByBuyer(User Buyer);

    public Order save(Order order);

    public Order findByBuyerAndDate(User Buyer, Date date);

    List<Order> findByPool(Pool pool);

    //todo check why selfpick=true not working
    //todo why
    @Query(value = "Select * from orders where buyer_id = ?1 and selfpick = 1 and status='placed'", nativeQuery = true)
    public List<Order> findByBuyerIdAndSelfpick(int buyerid);

    List<Order> findOrderByStatusAndPickedby(String status, User pickedByUser);

}
