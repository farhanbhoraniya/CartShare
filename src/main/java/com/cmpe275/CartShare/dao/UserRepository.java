package com.cmpe275.CartShare.dao;

import org.springframework.data.jpa.repository.JpaRepository;	
import com.cmpe275.CartShare.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByEmail(String email);
	public User save(User user);
	public User findById(int id);
}
