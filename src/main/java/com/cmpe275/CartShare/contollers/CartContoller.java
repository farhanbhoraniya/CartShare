package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.CartItemService;
import com.cmpe275.CartShare.service.CartService;
import com.cmpe275.CartShare.service.ProductService;
import com.cmpe275.CartShare.service.StoreService;

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
    CartItemService cartItemService;
    
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

    @PostMapping("/updateItemInCart")
    public @ResponseBody ResponseEntity<CartItem> addOrUpdateItemToCart(@RequestBody JSONObject cartItem) {
        if (! (cartItem.containsKey("store_id") && cartItem.containsKey("product_sku")
                && cartItem.containsKey("user_id") && cartItem.containsKey("quantity") && cartItem.containsKey("price"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        
        int store_id = (int) cartItem.get("store_id");
        String product_sku = (String) cartItem.get("product_sku");
        int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        System.out.println("User ID: "+user_id);
        int quantity = (int) cartItem.get("quantity");
        double price = Double.parseDouble(cartItem.get("price").toString());
        
        try{
            Cart cart= (Cart) cartService.findCartByUserId(user_id);

            if(cart==null){
                cart=cartService.createNewCart(user_id);
            }


            Store store = storeService.findById(store_id);
            Product product = productService.findProductInStore(store_id, product_sku);
            CartItem ci = new CartItem(store, product, cart, quantity, (double)quantity*price);
            if(quantity == 0) {
                cartItemService.delete(ci);
                return ResponseEntity.status(HttpStatus.OK).body(ci);
            }

            
            cartItemService.saveOrUpdateCartItem(ci);
            return ResponseEntity.status(HttpStatus.OK).body(ci);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        
    }
    
//    @PostMapping("/addItemToCart")
//    public @ResponseBody ResponseEntity<String> addItemToCart(@RequestBody JSONObject cartItem) {
//
//        if (! (cartItem.containsKey("store_id") && cartItem.containsKey("product_sku")
//                && cartItem.containsKey("user_id") && cartItem.containsKey("quantity") && cartItem.containsKey("price"))) {
//            System.out.println("Invalid or missing parameters");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//        int store_id = (int) cartItem.get("store_id");
//        String product_sku = (String) cartItem.get("product_sku");
//        int user_id = (int) cartItem.get("user_id");
//        int quantity = (int) cartItem.get("quantity");
//        double price = Double.parseDouble(cartItem.get("price").toString());
//
//        try{
//            Cart cart= (Cart) cartService.findCartByUserId(user_id);
//
//            if(cart==null){
//                cart=cartService.createNewCart(user_id);
//            }
//
//
//            Store store = storeService.findById(store_id);
//            Product product = productService.findProductInStore(store_id, product_sku);
//
//            CartItem ci = new CartItem(store, product, cart, quantity, quantity*price);
//            cartService.saveCartItem(ci);
//            return ResponseEntity.status(HttpStatus.OK).body("Item Added Successfully");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Item Add Unsuccessful");
//        }
//    }
//    
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
        modelAndView.setViewName("addToCart/viewCart");
        return  modelAndView;
    }
}
