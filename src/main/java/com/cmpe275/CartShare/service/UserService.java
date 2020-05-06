package com.cmpe275.CartShare.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.dao.UserRepository;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    //	@Autowired
    //	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User save(User user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        User newUser = userRepository.findByEmail(user.getEmail());
        return newUser;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }
}
