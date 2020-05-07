package com.cmpe275.CartShare.model;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private int id;
    //many carts can have same user id linked
    @ManyToOne
    @JoinColumn(name="user", referencedColumnName = "id")
    private User user;
    

    public Cart() {
    }

    public Cart(User user){
        this.user=user;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
