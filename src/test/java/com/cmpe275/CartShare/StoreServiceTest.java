package com.cmpe275.CartShare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmpe275.CartShare.dao.StoreRepository;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.service.StoreService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartShareApplication.class)
public class StoreServiceTest { 
	
	@Autowired
	private StoreService storeService;
	
	@MockBean
	private StoreRepository storeRepository;
	
	Store store = new Store("test", "street", "number", "city", "zip", "state");
	
	@Test
	public void findStoreById() {
		Mockito.when(storeRepository.findById(1)).thenReturn(store);
		Store storeNew = storeService.findById(1);
		
		assertEquals(store.getName(), "test");
	}
	
	@Test
	public void saveStore() {
		Mockito.when(storeRepository.save(Mockito.any())).thenReturn(store);
		Mockito.when(storeRepository.findByName(store.getName())).thenReturn(store);
		Store storeNew = storeService.save(new Store("test", "street", "number", "city", "zip", "state"));
		
		assertEquals(store.getName(), "test");
	}
	
	@Test
	public void deleteStore() {
		System.out.println("before");
		Mockito.when(storeRepository.deleteById(1)).thenReturn(store);
		Store storeNew = storeService.delete(1);
		
		assertEquals(store.getName(), "test");
	}

}
