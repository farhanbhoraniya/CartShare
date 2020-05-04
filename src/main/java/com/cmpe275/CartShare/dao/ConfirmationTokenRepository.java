package com.cmpe275.CartShare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer>{
	
	public ConfirmationToken findByConfirmationtoken(String confirmationToken);
	public ConfirmationToken save(ConfirmationToken confirmationToken);
	public void delete(ConfirmationToken confirmationToken);
	
}
