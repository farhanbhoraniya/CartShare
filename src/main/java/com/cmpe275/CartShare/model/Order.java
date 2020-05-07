package com.cmpe275.CartShare.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private int id;

    //todo make this many to one,if the buyer chooses to get other orders as well then--
    //todo --other orders must be linked somehow as well, could use list of cartIds
    @OneToOne
    @JoinColumn(name="cart", referencedColumnName = "id")
    private Cart cart;

    @OneToOne
    @JoinColumn(name="pool", referencedColumnName = "id")
    private Pool pool;

    private Date date;
    //todo make this ENUM
    private String status;

    @OneToOne
    @JoinColumn(name="pickedby", referencedColumnName = "id")
    private User pickedby;

    @OneToOne
    @JoinColumn(name="buyer", referencedColumnName = "id")
    private User buyer;

    private boolean selfPick;

    public Order() {
    }

    //todo skipping picekd for now, analyze later ,User pickedby
    public Order(Cart cart_id,Pool pool_id,Date date,String status,User buyerid, boolean selfPick) {
        this.cart=cart_id;
        this.pool=pool_id;
        this.date=date;
        //this.pickedby=pickedby;
        this.buyer=buyerid;
        this.selfPick=selfPick;
        this.status=status;
    }

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

    public Cart getCart_id() {
        return cart;
    }

    public void setCart_id(Cart cart_id) {
        this.cart = cart_id;
    }

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
        return selfPick;
    }

    public void setSelfPick(boolean selfPick) {
        this.selfPick = selfPick;
    }
}
