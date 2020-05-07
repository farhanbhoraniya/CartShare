package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.service.CartService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartContoller {

    @Autowired
    CartService cartService;

    /*
     * gets cart for user_id,isActive
     * else return new
     * */
    @GetMapping("/getCart/{user_id}")
    public @ResponseBody ResponseEntity<List<CartItem>> getCart(@PathVariable int user_id) {
        List<CartItem> cartItems = cartService.findCartByUserId(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(cartItems);
    }

    @PostMapping("/addItemToCart")
    public @ResponseBody ResponseEntity<String> addItemToCart(@RequestBody JSONObject cartItem) {

        return ResponseEntity.status(HttpStatus.OK).body("Item Added Successfully");
    }


}