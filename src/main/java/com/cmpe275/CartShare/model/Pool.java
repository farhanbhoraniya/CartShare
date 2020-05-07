package com.cmpe275.CartShare.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pool")
public class Pool {

    @Id
    private int id;

    private String name;
    private String neighborhood;
    private String description;
    private String zip;

    @OneToOne
    @JoinColumn(name = "leader", referencedColumnName = "id")
    private User leader;

    @Transient
    private List<User> members;

    public Pool() {}

    public Pool(String name, String neighborhood, String description, String zip, User leader) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.description = description;
        this.zip = zip;
        this.leader = leader;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
