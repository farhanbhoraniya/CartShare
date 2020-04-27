package com.cmpe275.CartShare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cmpe275.CartShare.model.Store;;

public interface StoreRepository extends JpaRepository<Store, Integer> {

	public Store findById(int id);
	public Store save(Store store);
	public Store findByName(String name);
	public List<Store> findAll();
	public Store deleteById(int id);
}
