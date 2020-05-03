package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.model.Pool;

@Service
public class PoolService {

	@Autowired
	PoolRepository poolRepository;
	
	public Pool findByName(String name) {
		return poolRepository.findByName(name);
	}
	
	public Pool findById(int id) {
		return poolRepository.findById(id);
	}
	
	public List<Pool> findByZip(String zip) {
		return poolRepository.findByZip(zip);
	}
	
	public List<Pool> findByNeighborhood(String neighborhood) {
		return poolRepository.findByNeighborhood(neighborhood);
	}
	
	public List<Pool> findAll() {
		return poolRepository.findAll();
	}
	
	@Transactional
	public Pool save(Pool pool) {
		poolRepository.save(pool);
		Pool newPool = poolRepository.findByName(pool.getName());
		return newPool;
	}
	
	@Transactional
	public void update(Pool pool) {
		poolRepository.save(pool);
	}
	
	@Transactional
	public void delete(int id) {
		poolRepository.deleteById(id);
	}
}
