package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    PoolRepository poolRepository;

    @Autowired
    PoolMembershipRepository poolMembershipRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public Optional<User> save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userRepository.findByEmail(user.getEmail());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Pool findUserPool(User user)
    {
        Pool pool = poolRepository.findByLeader(user);
        if(pool == null)
        {
            PoolMembership poolMembership = poolMembershipRepository.findByUser(user);
            if(poolMembership != null)
            {
                pool = poolRepository.findById(poolMembership.getPool());
            }
        }

        if(pool != null)
        {
            pool.setMembers(pool.getMembers());
        }

        return pool;
    }

    public List<User> findUsersByType(String type) {
        return userRepository.findByType(type);
        
    }
}
