package com.cmpe275.CartShare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer>{
	
	public UserAddress save(UserAddress userAddress);
	public UserAddress findByuser(User user);

}
