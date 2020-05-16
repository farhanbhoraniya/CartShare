package com.cmpe275.CartShare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.LinkedOrdersRepository;
import com.cmpe275.CartShare.model.LinkedOrders;

@Service
public class LinkedOrderService {
    
    @Autowired
    LinkedOrdersRepository linkedOrderRepostiory;

    public void save(LinkedOrders lOrders) {
        linkedOrderRepostiory.save(lOrders);
    }

}
