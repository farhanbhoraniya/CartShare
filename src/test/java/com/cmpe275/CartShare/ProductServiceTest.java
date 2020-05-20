package com.cmpe275.CartShare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmpe275.CartShare.dao.ProductRepository;
import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartShareApplication.class)
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	Product product = new Product("testsku", 1, "test product", "description", null, "brand", "unit", 1.2);
	
	@Test
	public void findProductByStore() {
		List<Product> productList = new ArrayList<Product>();
		productList.add(product);
		Mockito.when(productRepository.findByStoreid(1)).thenReturn(productList);
		List<Product> products = productService.findByStore(1);
		assertEquals(products.get(0).getSku(), productList.get(0).getSku());
	}
	
	@Test
	public void findByProduct() {
		List<Product> productList = new ArrayList<Product>();
		productList.add(product);
		Mockito.when(productRepository.findBySku("testsku")).thenReturn(productList);
		List<Product> products = productService.findByProduct("testsku");
		assertEquals(products.get(0).getSku(), productList.get(0).getSku());
	}
	
	@Test
	public void findProductInStore() {
		Mockito.when(productRepository.findBySkuAndStoreid("testsku", 1)).thenReturn(product);
		Product productNew = productService.findProductInStore(1, "testsku");
		assertEquals(product.getSku(), productNew.getSku());
	}
}
