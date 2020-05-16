package com.cmpe275.CartShare.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    //todo make this many to one,if the buyer chooses to get other orders as well then--
//    //todo --other orders must be linked somehow as well, could use list of cartIds
//    @OneToOne
//    @JoinColumn(name="cart", referencedColumnName = "id")
//    private Cart cart;

    @OneToOne
    @JoinColumn(name="pool_id", referencedColumnName = "id")
    private Pool pool;

    private Date date;
    //todo make this ENUM
    private String status;

    @OneToOne
    @JoinColumn(name="pickedby", referencedColumnName = "id")
    private User pickedby;

    @OneToOne
    @JoinColumn(name="buyer_id", referencedColumnName = "id")
    private User buyer;

    @Column(name = "selfpick")
    private boolean selfpick;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"order"})
    private List<OrderItems> orderItems;

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public Order() {
    }

    //todo skipping picekd for now, analyze later ,User pickedby
    public Order(Pool pool_id,Date date,String status,User buyerid, User pickedby, boolean selfPick) {
//        this.cart=cart_id;
        this.pool=pool_id;
        this.date=date;
        this.pickedby=pickedby;
        this.buyer=buyerid;
        this.selfpick=selfPick;
        this.status=status;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBuyerid() {
        return buyer;
    }

    public void setBuyerid(User buyerid) {
        this.buyer = buyerid;
    }

//    public Cart getCart_id() {
//        return cart;
//    }
//
//    public void setCart_id(Cart cart_id) {
//        this.cart = cart_id;
//    }

    public Pool getPool_id() {
        return pool;
    }

    public void setPool_id(Pool pool_id) {
        this.pool = pool_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getPickedby() {
        return pickedby;
    }

    public void setPickedby(User pickedby) {
        this.pickedby = pickedby;
    }

    public boolean isSelfPick() {
        return selfpick;
    }

    public void setSelfPick(boolean selfPick) {
        this.selfpick = selfPick;
    }

}
