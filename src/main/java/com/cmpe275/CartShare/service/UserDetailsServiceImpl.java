package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        String roleName = user.getType();
        grantedAuthorities.add(new SimpleGrantedAuthority(roleName));

        LOGGER.error(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

}
