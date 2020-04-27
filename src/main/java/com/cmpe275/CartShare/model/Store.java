package com.cmpe275.CartShare.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stores")
public class Store {

	@Id
	private int id;
	private String name;
	private String streetname;
	private int streetnumber;
	private String city;
	private String zip;
	
	public Store() {}
	
	public Store(String name, String streetname, int streetnumber, String city, String zip) {
		this.name = name;
		this.streetname = streetname;
		this.streetnumber = streetnumber;
		this.city = city;
		this.zip = zip;
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
	
	public int getStreetnumber() {
		return streetnumber;
	}
	
	public void setStreetnumber(int streetnumber) {
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
}
