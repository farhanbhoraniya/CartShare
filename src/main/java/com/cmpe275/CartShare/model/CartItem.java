package com.cmpe275.CartShare.model;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    private int id;
    @OneToOne
    @JoinColumn(name="store", referencedColumnName = "id")
    private Store store;

    @OneToOne
    @JoinColumn(name="product", referencedColumnName = "id")
    private Product product;

    @OneToOne
    @JoinColumn(name="cart", referencedColumnName = "id")
    private Cart cart;

    private int quantity;
    private float price;

    public CartItem() {
    }
    public CartItem(int id, Store store, Product product_id, Cart cart, int quantity, float price) {
        this.id=id;
        this.store=store;
        this.product=product_id;
        this.cart=cart;
        this.quantity=quantity;
        this.price=price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
