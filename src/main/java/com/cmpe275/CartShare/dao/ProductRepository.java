package com.cmpe275.CartShare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cmpe275.CartShare.model.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	public List<Product> findBySku(String sku);
	public List<Product> findByStoreid(int id);
	public List<Product> findByName(String name);
	public Product save(Product product);
	public Product findBySkuAndStoreid(String sku, int id);
	public void deleteBySkuAndStoreid(String sku, int storeid);
}
