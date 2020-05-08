package com.cmpe275.CartShare.contollers;

import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.model.UserAddress;
import com.cmpe275.CartShare.service.UserAddressService;
import com.cmpe275.CartShare.service.UserService;

@RestController
public class UserAddressController {

	@Autowired
	UserAddressService userAddressService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/useraddress")
	public ResponseEntity<UserAddress> createAddress(@RequestBody JSONObject addressObject) {
		
		if(!addressObject.containsKey("user")) {
			System.out.println("User must be present");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		int userId = (int)addressObject.get("user");
		
		User userObject = userService.findById(userId);
		
		if (userObject == null) {
			System.out.println("User does not exists");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		String streetName = null;
		String streetNo = null;
		String city = null;
		String state = null;
		String zip = null;
		
		if(addressObject.containsKey("streetName")) {
			streetName = (String)addressObject.get("streetName");
		}
		
		if(addressObject.containsKey("streetNo")) {
			streetNo = (String)addressObject.get("streetNo");
		}
		
		if(addressObject.containsKey("city")) {
			city = (String)addressObject.get("city");
		}
		
		if(addressObject.containsKey("state")) {
			state = (String)addressObject.get("state");
		}
		
		if(addressObject.containsKey("zip")) {
			zip = (String)addressObject.get("zip");
		}
		
		UserAddress userAddress = new UserAddress(userObject, streetName, streetNo, city, state, zip);
		
		UserAddress newAddress = userAddressService.save(userAddress);
		
		return ResponseEntity.status(HttpStatus.OK).body(newAddress);
	}
	
	@GetMapping("/user/{userId}/address")
	public ResponseEntity<UserAddress> getUserAddress(@PathVariable int userId) {
		
		User userObject = userService.findById(userId);
		
		if (userObject == null) {
			System.out.println("User does not exists");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		UserAddress userAddress = userAddressService.findByUser(userObject);
		
		return ResponseEntity.status(HttpStatus.OK).body(userAddress);
		
	}
}
