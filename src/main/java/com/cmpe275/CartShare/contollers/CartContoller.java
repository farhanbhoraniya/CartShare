package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.service.CartService;
import com.cmpe275.CartShare.service.ProductService;
import com.cmpe275.CartShare.service.StoreService;

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
    
    @Autowired
    ProductService productService;

    @Autowired
    StoreService storeService;
    
    
    /*
     * gets cart for user_id,isActive
     * else return new
     * */
    @GetMapping("/getCart/{user_id}")
    public @ResponseBody ResponseEntity<List<CartItem>> getCart(@PathVariable int user_id) {
        return null;
//        List<CartItem> cartItems = cartService.findCartByUserId(user_id);
//        return ResponseEntity.status(HttpStatus.OK).body(cartItems);
    }

    @PostMapping("/addItemToCart")
    public @ResponseBody ResponseEntity<String> addItemToCart(@RequestBody JSONObject cartItem) {
        
        if (! (cartItem.containsKey("store_id") && cartItem.containsKey("product_sku") 
                && cartItem.containsKey("user_id") && cartItem.containsKey("quantity") && cartItem.containsKey("price"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        int store_id = (int) cartItem.get("store_id");
        String product_sku = (String) cartItem.get("product_sku");
        int user_id = (int) cartItem.get("user_id");
        int quantity = (int) cartItem.get("quantity");
        double price = (double) cartItem.get("price");
        
        Cart cart= (Cart) cartService.findCartByUserId(user_id);
        
        
        
        Store store = storeService.findById(store_id);
        Product product = productService.findProductInStore(store_id, product_sku);
        
        CartItem ci = new CartItem(store, product, cart, quantity, price);
        cartService.saveCartItem(ci);
        return ResponseEntity.status(HttpStatus.OK).body("Item Added Successfully");
    }
    
    @PostMapping("/removeItemFromCart/{cart_item_id}")
    public @ResponseBody ResponseEntity<String> addItemToCart(@PathVariable String cartItem) {
        
        return null;
    }
    
    @PostMapping("/placeOrder")
    public @ResponseBody ResponseEntity<String> placeOrder(@PathVariable String orderObject) {
        
        return null;
    }
}