package com.cmpe275.CartShare.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "products")
@IdClass(ProductPK.class)
public class Product {

	@Id
	private String sku;
	
	@Id
	private int storeid;
	
	private String name;
	private String description;
	private String imageurl;
	private String brand;
	private String unit;
	private double price;
	
	public Product() {}
	public Product(String sku, int storeid, String name, String description, String imageurl, String brand, String unit, double price) {
		this.sku = sku;
		this.storeid = storeid;
		this.name = name;
		this.description = description;
		this.imageurl = imageurl;
		this.brand = brand;
		this.unit = unit;
		this.price = price;	
	}
	
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public int getStoreid() {
		return storeid;
	}
	
	public void setStoreid(int storeid) {
		this.storeid = storeid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImageurl() {
		return imageurl;
	}
	
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product{" +
				"sku='" + sku + '\'' +
				", storeid=" + storeid +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", imageurl='" + imageurl + '\'' +
				", brand='" + brand + '\'' +
				", unit='" + unit + '\'' +
				", price=" + price +
				'}';
	}
}
