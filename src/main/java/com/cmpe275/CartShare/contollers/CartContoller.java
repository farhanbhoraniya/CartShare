package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.CartItemService;
import com.cmpe275.CartShare.service.CartService;
import com.cmpe275.CartShare.service.PoolMembershipService;
import com.cmpe275.CartShare.service.PoolService;
import com.cmpe275.CartShare.service.ProductService;
import com.cmpe275.CartShare.service.StoreService;
import com.cmpe275.CartShare.service.UserService;

import net.minidev.json.JSONArray;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CartContoller {

    @Autowired
    CartService cartService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    PoolService poolService;
    
    @Autowired
    CartItemService cartItemService;
    
    @Autowired
    ProductService productService;

    @Autowired
    StoreService storeService;
    
    @Autowired
    PoolMembershipService poolMembershipService;
    
    @PostMapping("/updateItemInCart")
    public @ResponseBody ResponseEntity<String> addOrUpdateItemToCart(@RequestBody JSONObject cartItem) {
        if (! (cartItem.containsKey("store_id") && cartItem.containsKey("product_sku")
                && cartItem.containsKey("user_id") && cartItem.containsKey("quantity") && cartItem.containsKey("price"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        int store_id = (int) cartItem.get("store_id");
        String product_sku = (String) cartItem.get("product_sku");
        int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User currentUserObject = userService.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", user_id));
        int quantity = (int) cartItem.get("quantity");
        double price = Double.parseDouble(cartItem.get("price").toString());
        
        JSONObject obj = new JSONObject();
        try{
            Pool pool = poolService.findByLeader(currentUserObject);

            if (pool == null) {
                PoolMembership poolMembership = poolMembershipService.findByUser(currentUserObject);

                if (poolMembership == null) {
                    obj.put("code", "300");
                    obj.put("message", "You need to be a member of a pool to add items in cart");
                    return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
                }
                pool = poolService.findById(poolMembership.getPool());
            }
            
            Cart cart= (Cart) cartService.findCartByUserId(user_id);

            if(cart==null){
                cart=cartService.createNewCart(user_id); 
            }
            else {
                boolean delPrevItems = (boolean) cartItem.get("delPrevItems");
                if(delPrevItems) {
                    cartItemService.deleteByCart(cart);
                }else {
                //check if the cart contains items from another store
                    List<CartItem> cartItems = cartItemService.findByCart(cart.getId());
                    if(cartItems.size() > 0) {
                        if(cartItems.get(0).getProduct().getStoreid() != store_id) {
                            obj.put("code", "500");
                            obj.put("message", "Override Cart?");
                            return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
                        }
                    }
                }
            }

            Store store = storeService.findById(store_id);
            Product product = productService.findProductInStore(store_id, product_sku);
            
            CartItem ci = new CartItem(store, product, cart, quantity, (double)quantity*price);
            if(quantity == 0) {
                cartItemService.delete(ci);
                obj.put("code", "200");
                obj.put("message", "Item Deleted from Cart");
                return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
            }

            cartItemService.saveOrUpdateCartItem(ci);
            obj.put("code", "200");
            obj.put("message", "Cart Updated");
            obj.put("price",ci.getPrice());
            return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        
    }
    
    @DeleteMapping("/removeItemFromCart/{cartItem}")
    public @ResponseBody ResponseEntity<String> removeItemFromCart(@PathVariable String cartItem) {
        int cartItemId = Integer.parseInt(cartItem);
        try{
            cartService.removeCartItem(cartItemId);
            return ResponseEntity.status(HttpStatus.OK).body("Item Deletion Successful");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Item Deletion Unsuccessful");
        }
    }
    
    
    
    @GetMapping("/getOrdersFromCart/{user_id}")
    public ModelAndView getStoreProducts(ModelAndView modelAndView, 
                                                    @PathVariable int user_id) {

        int userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<CartItem> items = new ArrayList<CartItem>();
        
        Cart cart= (Cart) cartService.findCartByUserId(userId);
        
        items = (ArrayList<CartItem>)cartService.findByCartId(cart);
        
        JSONArray array = new JSONArray();
        double subtotal=0.0;
        int storeId = 0;
        for(CartItem item: items) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getProduct().getName());
            obj.put("desc", item.getProduct().getDescription());
            obj.put("sku", item.getProduct().getSku());
            obj.put("price", item.getPrice());
            obj.put("per_item_price", item.getProduct().getPrice());
            obj.put("qty", item.getQuantity());
            obj.put("storeId", item.getProduct().getStoreid());
            obj.put("unit", item.getProduct().getUnit());
            obj.put("cart_item_id", item.getId());
            obj.put("imageurl", item.getProduct().getImageurl());
            subtotal += item.getPrice();
            storeId = item.getProduct().getStoreid();
            array.add(obj);
        }
        
        
        BigDecimal bd1 = new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP);
        subtotal = bd1.doubleValue();
        
        double tax = subtotal*(9.25/100);
        bd1 = new BigDecimal(tax).setScale(2, RoundingMode.HALF_UP);
        tax = bd1.doubleValue();
        
        double con = subtotal*(0.5/100);
        bd1 = new BigDecimal(con).setScale(2, RoundingMode.HALF_UP);
        con = bd1.doubleValue();
        
        double total = subtotal+tax+con;
        bd1 = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
        total = bd1.doubleValue();

        modelAndView.addObject("items", array);
        modelAndView.addObject("subtotal", subtotal);
        modelAndView.addObject("tax", tax);
        modelAndView.addObject("con", con);
        modelAndView.addObject("total", total);
        modelAndView.addObject("storeId", storeId);
        modelAndView.setViewName("addToCart/viewCart");
        return  modelAndView;
    }
}
