package com.cmpe275.CartShare.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"orderItems"})
    private Order order;

    @OneToOne
    @JoinColumns({
            @JoinColumn(
                    name = "product_sku",
                    referencedColumnName = "sku"),
            @JoinColumn(
                    name = "store_id",
                    referencedColumnName = "storeid")
    })

    private Product product;

    private int qty;
    private double price;
    
    public OrderItems() {
    }
    public OrderItems(Product product, Order order, int quantity, double price) {
        //this.store=store;
        this.product = product;
        this.order = order;
        this.qty = qty;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}