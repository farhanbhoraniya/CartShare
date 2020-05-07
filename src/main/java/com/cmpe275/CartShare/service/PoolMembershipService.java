package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.model.PoolMembership;

@Service
public class PoolMembershipService {

	@Autowired
	PoolMembershipRepository poolMembershipRepository;
	
	public List<PoolMembership> findByPool(String pool) {
		return poolMembershipRepository.findByPool(pool);
	}
	
	public PoolMembership findByUser(int user) {
		return poolMembershipRepository.findByUser(user);
	}
	
	public PoolMembership membershipCheck(String pool, int user) {
		return poolMembershipRepository.findByPoolAndUser(pool, user);
	}
	
	@Transactional
	public void save(PoolMembership poolMembership) {
		poolMembershipRepository.save(poolMembership);
	}
	
	@Transactional
	public void delete(int user) {
		poolMembershipRepository.deleteByUser(user);
	}
}
