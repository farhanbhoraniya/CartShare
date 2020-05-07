package com.cmpe275.CartShare.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    private int id;
    private String name;
    private String streetname;
    private String streetnumber;
    private String city;
    private String zip;
    private String state;

    @OneToMany(mappedBy = "storeid", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"store"})
    private Set<Product> products;

    public Store() {}

    public Store(String name, String streetname, String streetnumber, String city, String zip, String state) {
        this.name = name;
        this.streetname = streetname;
        this.streetnumber = streetnumber;
        this.city = city;
        this.zip = zip;
        this.state = state;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", streetname='" + streetname + '\'' +
                ", streetnumber='" + streetnumber + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", products=" + products +
                '}';
    }
}
