package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoolRepository extends JpaRepository<Pool, Integer> {

    public Pool findById(String id);

    public Pool findByName(String name);

    public List<Pool> findByZip(String zip);

    public List<Pool> findByNeighborhood(String neighborhood);

    public List<Pool> findAll();

    public Pool findByLeader(User leader);

    public Pool save(Pool pool);

    public void deleteById(String id);

}
