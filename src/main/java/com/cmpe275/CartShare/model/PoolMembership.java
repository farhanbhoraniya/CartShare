package com.cmpe275.CartShare.model;

import javax.persistence.*;

@Entity
@Table(name = "pool_membership")
public class PoolMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String pool;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    private int reference;
    private boolean verified;
    private boolean leaderapproved;

    public PoolMembership() {}

    public PoolMembership(String pool, User user, int reference, boolean verified, boolean leaderapproved) {
        this.pool = pool;
        this.user = user;
        this.reference = reference;
        this.verified = verified;
        this.setLeaderapproved(leaderapproved);
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isLeaderapproved() {
        return leaderapproved;
    }

    public void setLeaderapproved(boolean leaderapproved) {
        this.leaderapproved = leaderapproved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
