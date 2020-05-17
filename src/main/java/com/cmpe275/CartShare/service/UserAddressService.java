package com.cmpe275.CartShare.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.CartShare.dao.UserAddressRepository;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.model.UserAddress;

@Service
public class UserAddressService {

	@Autowired
	UserAddressRepository userAddressRepository;
	
	@Transactional
	public UserAddress save(UserAddress userAddress) {
		return userAddressRepository.save(userAddress);
	}
	
	public UserAddress findByUser(User user) {
		return userAddressRepository.findByuser(user);
	}

    public UserAddress update(UserAddress userAddress, String streetname, String streetno, String city, String state, String zip) {
        
        userAddress.setCity(city); 
        userAddress.setState(state);
        userAddress.setZip(zip);
        userAddress.setStreetname(streetname);
        userAddress.setStreetno(streetno);
        return save(userAddress);
    }
}
