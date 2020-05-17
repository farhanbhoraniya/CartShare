package com.cmpe275.CartShare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_address")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "street_name")
    private String streetname;

    @Column(name = "street_no")
    private String streetno;

    private String city;
    private String state;
    private String zip;

    public UserAddress() {
        
    }

    public UserAddress(User user, String streetname, String streetno, String city, String state, String zip) {
        this.user = user;
        this.streetname = streetname;
        this.streetno = streetno;
        this.city = city;
        this.state = state;
        this.zip = zip;
        
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
    public String getStreetname() {
        return streetname;
    }
    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }
    public String getStreetno() {
        return streetno;
    }
    public void setStreetno(String streetno) {
        this.streetno = streetno;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", streetname='" + streetname + '\'' +
                ", streetno='" + streetno + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip=" + zip +
                '}';
    }
}
