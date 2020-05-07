package com.cmpe275.CartShare.contollers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.model.Cart;
import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.OrderItems;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.CartItemService;
import com.cmpe275.CartShare.service.CartService;
import com.cmpe275.CartShare.service.OrderItemsService;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.PoolMembershipService;
import com.cmpe275.CartShare.service.PoolService;
import com.cmpe275.CartShare.service.UserService;

@RestController
public class OrderController {

	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	PoolService poolService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	PoolMembershipService poolMembershipService;
	
	@Autowired
	OrderItemsService orderItemsService;
	
	@PostMapping("/order/place")
	public ResponseEntity<Order> placeOrder(@RequestBody JSONObject requestBody) {
		// TODO: GET CURRENT LOGIN USER
//      org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//      System.out.println(principal);
//      User currentUserObject = userService.findByEmail(principal.getUsername());
		
		if(!requestBody.containsKey("selfPick")) {
			System.out.println("Invalid or missing paramters");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		User currentUserObject = userService.findById(46);
		Cart cart = cartService.findCartByUserId(currentUserObject.getId());
		Pool pool = poolService.findByLeader(currentUserObject);
		
		if (pool == null) {
			PoolMembership poolMembership = poolMembershipService.findByUser(currentUserObject.getId());
			
			if (poolMembership == null) {
				System.out.println("User is not a member of any pool");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			pool = poolService.findById(poolMembership.getPool());
		}
		
		Date date = new Date();  
		String status = "PLACED";
		User pickedBy = null;
		boolean selfPick = (boolean)requestBody.get("selfPick");
		if(selfPick) {
			pickedBy = currentUserObject;
		}
		User buyer = currentUserObject;
		
		Order order = new Order(pool, date, status, buyer, pickedBy, selfPick);
		Order newOrder = orderService.save(order);
		
		System.out.println("Order placed");
		System.out.println(newOrder.getId());
		
		List<CartItem> cartItems = cartItemRepository.findByCart(cart);
		
		for(CartItem cartItem: cartItems) {
			OrderItems orderItem = new OrderItems(cartItem.getProduct(), newOrder, cartItem.getQuantity(), cartItem.getPrice());
			System.out.println("Moving item");
			orderItemsService.save(orderItem);
			cartItemService.delete(cartItem);
		}
		
		System.out.println("Items moved to the order items table");
				
//		cartItemService.deleteAll(cartItems);
		
		System.out.println("Items removed from cart items");
		return ResponseEntity.status(HttpStatus.OK).body(newOrder);
		
	}
}
