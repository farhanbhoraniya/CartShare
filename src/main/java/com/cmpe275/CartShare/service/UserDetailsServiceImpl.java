package com.cmpe275.CartShare.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		
		Set<GrantedAuthority> grantedAutorities = new HashSet<GrantedAuthority>();
		
		grantedAutorities.add(new SimpleGrantedAuthority(user.getType()));
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAutorities);
	}

}
