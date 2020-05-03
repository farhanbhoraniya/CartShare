package com.cmpe275.CartShare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.PoolMembership;

public interface PoolMembershipRepository extends JpaRepository<PoolMembership, Integer>{
	
	public PoolMembership save(PoolMembership poolMembership);
	public List<PoolMembership> findByPool(int pool);
	public PoolMembership findByUser(int user);
	public PoolMembership findByPoolAndUser(int pool, int user);
	public void deleteByUser(int user);
}
