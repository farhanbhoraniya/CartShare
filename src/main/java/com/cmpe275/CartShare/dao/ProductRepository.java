package com.cmpe275.CartShare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cmpe275.CartShare.model.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	public List<Product> findBySku(String sku);
	public List<Product> findByStoreid(int store);
	public List<Product> findByName(String name);
	public Product save(Product product);
	public Product findBySkuAndStoreid(String sku, int store);
	public void deleteBySkuAndStoreid(String sku, int store);
	public void deleteByStoreid(int store);
}
