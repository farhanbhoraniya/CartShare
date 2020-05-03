package com.cmpe275.CartShare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.Pool;

public interface PoolRepository extends JpaRepository<Pool, Integer>{

	public Pool findById(int id);
	public Pool findByName(String name);
	public List<Pool> findByZip(String zip);
	public List<Pool> findByNeighborhood(String neighborhood);
	public List<Pool> findAll();
	public Pool save(Pool pool);
	public void deleteById(int id);
}
