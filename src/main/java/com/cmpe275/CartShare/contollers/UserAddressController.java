package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.model.UserAddress;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.PoolService;
import com.cmpe275.CartShare.service.UserAddressService;
import com.cmpe275.CartShare.service.UserService;

@RestController
public class UserAddressController {

    @Autowired
    UserAddressService userAddressService;

    @Autowired
    UserService userService;
    
    @Autowired
    PoolService poolService;

    @PostMapping("/useraddress/{id}")
    public ResponseEntity<UserAddress> createAddress(@PathVariable int id, @RequestBody JSONObject addressObject) {
        int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User userObject = userService.findById(user_id).orElseThrow(()-> new ResourceNotFoundException("User", "id", user_id));

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

        UserAddress userAddress = userAddressService.findByUser(userObject);

        UserAddress newAddress = userAddressService.update(userAddress,streetNo,streetName,city,state,zip);

        return ResponseEntity.status(HttpStatus.OK).body(newAddress);
    }

    @GetMapping("/user/{userId}/address")
    public ResponseEntity<UserAddress> getUserAddress(@PathVariable int userId) {

        User userObject = userService.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));

        if (userObject == null) {
            System.out.println("User does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        UserAddress userAddress = userAddressService.findByUser(userObject);

        return ResponseEntity.status(HttpStatus.OK).body(userAddress);

    }
    
    @GetMapping(value = "/pooler/getUserInformation")
    public ModelAndView getUserInformation(ModelAndView modelAndView) {
        int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User userObject = userService.findById(user_id).orElseThrow(()-> new ResourceNotFoundException("User", "id", user_id));
        UserAddress userAddress = userAddressService.findByUser(userObject);
        if(userAddress == null) {
            Optional<User> user = userService.findById(user_id);
            userAddress = new UserAddress(user.get(), null, null, null, null, null);
            userAddressService.save(userAddress);
        }
        boolean poolLeader = false;
        Pool pool = poolService.findByLeader(userObject);
        System.out.print(pool);
        if(pool != null) {
            poolLeader = true;
        }
        modelAndView.addObject("userInfo", userAddress);
        modelAndView.addObject("poolLeader", poolLeader);
        modelAndView.setViewName("user/profile");
        return modelAndView;
    }
}
