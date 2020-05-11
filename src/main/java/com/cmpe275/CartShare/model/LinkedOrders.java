package com.cmpe275.CartShare.model;

import javax.persistence.*;

@Entity
@Table(name = "linkedorders")
public class LinkedOrders {

    @Id
    private int id;

    //represents the order of id of the parent order
    private int parent_id;

    //represents the order linked to the parent order
    @OneToOne()
    @JoinColumn(name="pool_order", referencedColumnName = "id")
    private Order pool_order;

    public LinkedOrders() {
    }
    public LinkedOrders(int parent_id, Order pool_order) {
        this.parent_id=parent_id;
        this.pool_order=pool_order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public Order getPool_order() {
        return pool_order;
    }

    public void setPool_order(Order pool_order) {
        this.pool_order = pool_order;
    }
}
