package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.ProductRepository;
import com.cmpe275.CartShare.model.Product;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> findByStore(int storeid) {
		return productRepository.findByStoreid(storeid);
	}
	
	public List<Product> findByProduct(String sku) {
		return productRepository.findBySku(sku);
	}
	
	public List<Product> findByName(String name) {
		return productRepository.findByName(name);
	}
	
	@Transactional
	public void save(Product product) throws DataIntegrityViolationException {
		productRepository.save(product);
	}
	
	@Transactional
	public void saveMultiple(List<Product> products) throws DataIntegrityViolationException {
		for(Product product: products) {
			productRepository.save(product);
		}
	}
	
	public Product findProductInStore(int storeId, String sku) {
		return productRepository.findBySkuAndStoreid(sku, storeId);
	}
	
	@Transactional
	public void delete(int storeid, String sku) {
		productRepository.deleteBySkuAndStoreid(sku, storeid);
	}
	
	@Transactional
	public void deleteByStore(int storeid) {
		productRepository.deleteByStoreid(storeid);
	}
}
