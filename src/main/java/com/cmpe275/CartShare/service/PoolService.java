package com.cmpe275.CartShare.service;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PoolService {

    @Autowired
    PoolRepository poolRepository;

    @Autowired
    PoolMembershipRepository poolMembershipRepository;

    @Autowired
    UserRepository userRepository;

    private List<PoolMembership> getMembers(String pool) {
        List<PoolMembership> poolMemberships = poolMembershipRepository.findByPool(pool);
        return poolMemberships;
//        List<User> poolMembers = new ArrayList<User>();
//
//        for (PoolMembership poolMembership : poolMemberships) {
//            Optional<User> optionalUser = userRepository.findById(poolMembership.getUser());
//            optionalUser.ifPresent(poolMembers::add);
//        }
//        return poolMembers;
    }

    public Pool findByName(String name) {
        Pool pool = poolRepository.findByName(name);
        if(pool != null)
        {
            List<PoolMembership> poolMembers = getMembers(pool.getId());
            pool.setMembers(poolMembers);
        }
        return pool;
    }

    public Pool findByLeader(User user) {
        return poolRepository.findByLeader(user);
    }

    public Pool findById(String id) {
        Pool pool = poolRepository.findById(id);
        if(pool != null)
        {
            List<PoolMembership> poolMembers = getMembers(pool.getId());
            pool.setMembers(poolMembers);
        }
        return pool;
    }

    public List<Pool> findByZip(String zip) {
        List<Pool> pools = poolRepository.findByZip(zip);
        for (int i = 0; i < pools.size(); i++) {

            List<PoolMembership> poolMembers = getMembers(pools.get(i).getId());
            pools.get(i).setMembers(poolMembers);
        }

        return pools;
    }

    public List<Pool> findByNeighborhood(String neighborhood) {
        List<Pool> pools = poolRepository.findByNeighborhood(neighborhood);
        for (int i = 0; i < pools.size(); i++) {

            List<PoolMembership> poolMembers = getMembers(pools.get(i).getId());
            pools.get(i).setMembers(poolMembers);
        }

        return pools;
    }

    public List<Pool> findAll() {
        List<Pool> pools = poolRepository.findAll();
        for (int i = 0; i < pools.size(); i++) {

            List<PoolMembership> poolMembers = getMembers(pools.get(i).getId());
            pools.get(i).setMembers(poolMembers);
        }
        return pools;
    }

    @Transactional
    public Pool save(Pool pool) {
        poolRepository.save(pool);
        Pool newPool = poolRepository.findByName(pool.getName());
        return newPool;
    }

    @Transactional
    public void update(Pool pool) {
        poolRepository.save(pool);
    }

    @Transactional
    public void delete(String id) {
        poolRepository.deleteById(id);
    }
}
