package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.OrderItems;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.OrderItemsService;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.StoreService;
import com.cmpe275.CartShare.service.UserService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyOrdersController {

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderItemsService orderItemService;
	
	@Autowired
	StoreService storeService;
	
    @GetMapping("/myOrders")
    public ModelAndView getOrdersView(ModelAndView modelAndView) {


//        User user = userService.findById(userId);
//        List<Order> orders = orderService.getSelfPickOrders(true, user);
//
        modelAndView.setViewName("myOrders/index");
//        modelAndView.addObject("orders", orders);
        return modelAndView;
    }
    
    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<List<JSONObject>> getUserOrders(@PathVariable int userId) {
    	
    	User user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));;
    	if(user == null) {
    		System.out.println("User does not exists");
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
    	
    	List<Order> userOrders = orderService.getUserOrders(user);
    	List<JSONObject> orders = new ArrayList<JSONObject>();
    	
    	for(Order order: userOrders) {
//    		String temp = userOrders.toString();
    		JSONObject tempJSON = new JSONObject();
    		List<OrderItems> orderItems = orderItemService.getOrderItems(order);
    		double bill = 0;
    		for(OrderItems orderItem: orderItems) {
    			bill += orderItem.getPrice();
    		}
    		if(orderItems.size() == 0) {
    			continue;
    		}
    		tempJSON.put("status", order.getStatus());
    		tempJSON.put("id", order.getId());
    		tempJSON.put("totalItems", orderItems.size());
    		tempJSON.put("totalBill", bill);
    		int storeId = orderItems.get(0).getProduct().getStoreid();
    		
    		Store store = storeService.findById(storeId);
    		String storeName;
    		if (store == null) {
    			storeName = "DELETED";
    		} else {
    			storeName = store.getName();
    		}
    		
    		tempJSON.put("store", storeName);
    		
    		orders.add(tempJSON);
    	
    	}
    	return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

}
