package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.LinkedOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkedOrdersRepository extends JpaRepository<LinkedOrders, Integer> {
    //todo why is this method name not working as per the name

    @Query(value="Select * from linkedOrders where parent_id = ?1", nativeQuery = true)
    public List<LinkedOrders> findAllByParent_id(int parentid);

    public void persist(LinkedOrders linkedOrders);
}
