package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.StoreRepository;
import com.cmpe275.CartShare.model.Store;

@Service
public class StoreService {
	
	@Autowired
	StoreRepository storeRepository;
	
	@Transactional
	public Store save(Store store) {
		storeRepository.save(store);
		Store newStore = storeRepository.findByName(store.getName());
		return newStore;
	}
	
	public List<Store> findAll() {
		return storeRepository.findAll();
	}
	
	public Store findById(int id) {
		return storeRepository.findById(id);
	}

	@Transactional
	public Store update(Store store) {
		return storeRepository.save(store);
	}
	
	public Store delete(int storeId) {
		return storeRepository.deleteById(storeId);
	}
}
