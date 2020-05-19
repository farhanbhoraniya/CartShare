package com.cmpe275.CartShare.service;

import java.util.List;

import javax.transaction.Transactional;

import com.cmpe275.CartShare.model.User;
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

	public PoolMembership findById(int id) {
		return poolMembershipRepository.findById(id);
	}

	public PoolMembership findByUser(User user) {
		return poolMembershipRepository.findByUser(user);
	}
	
	public PoolMembership membershipCheck(String pool, User user) {
		return poolMembershipRepository.findByPoolAndUser(pool, user);
	}
	
	@Transactional
	public void save(PoolMembership poolMembership) {
		poolMembershipRepository.save(poolMembership);
	}
	
	@Transactional
	public void delete(User user) {
		poolMembershipRepository.deleteByUser(user);
	}

	@Transactional
	public void deleteById(int id){
		poolMembershipRepository.deleteById(id);
	}
}