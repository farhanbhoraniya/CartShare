package com.cmpe275.CartShare.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;

@Service
public class PoolService {

    @Autowired
    PoolRepository poolRepository;

    @Autowired
    PoolMembershipRepository poolMembershipRepository;

    @Autowired
    UserRepository userRepository;

    private List<User> getMembers(int pool) {
        List<PoolMembership> poolMemberships = poolMembershipRepository.findByPool(pool);
        List<User> poolMembers = new ArrayList<User>();

        for(PoolMembership poolMembership : poolMemberships) {
            poolMembers.add(userRepository.findById(poolMembership.getUser()));
        }
        return poolMembers;
    }

    public Pool findByName(String name) {
        Pool pool = poolRepository.findByName(name); 
        if(pool != null) {
            List<User> poolMembers = getMembers(pool.getId());
            pool.setMembers(poolMembers);
        }
        return pool;
    }

    public Pool findById(int id) {
        Pool pool = poolRepository.findById(id);
        List<User> poolMembers = getMembers(pool.getId());
        pool.setMembers(poolMembers);
        return pool;
    }

    public List<Pool> findByZip(String zip) {
        List<Pool> pools = poolRepository.findByZip(zip);
        for(int i=0; i<pools.size(); i++) {

            List<User> poolMembers = getMembers(pools.get(i).getId());
            pools.get(i).setMembers(poolMembers);			
        }

        return pools;
    }

    public List<Pool> findByNeighborhood(String neighborhood) {
        List<Pool> pools = poolRepository.findByNeighborhood(neighborhood);
        for(int i=0; i<pools.size(); i++) {

            List<User> poolMembers = getMembers(pools.get(i).getId());
            pools.get(i).setMembers(poolMembers);			
        }

        return pools;
    }

    public List<Pool> findAll() {
        List<Pool> pools = poolRepository.findAll();
        for(int i=0; i<pools.size(); i++) {

            List<User> poolMembers = getMembers(pools.get(i).getId());
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
    public void delete(int id) {
        poolRepository.deleteById(id);
    }
}
