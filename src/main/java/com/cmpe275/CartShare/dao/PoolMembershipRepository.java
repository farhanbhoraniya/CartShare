package com.cmpe275.CartShare.dao;

import java.util.List;

import com.cmpe275.CartShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.PoolMembership;

public interface PoolMembershipRepository extends JpaRepository<PoolMembership, Integer>{
	
	public PoolMembership save(PoolMembership poolMembership);
	public List<PoolMembership> findByPool(String pool);
	public PoolMembership findByUser(User user);
	public PoolMembership findById(int id);
	public PoolMembership findByPoolAndUser(String pool, User user);
	public void deleteByUser(User user);
	public void deleteById(int id);
}
