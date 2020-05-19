package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.async.MailAsyncComponent;
import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.*;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.*;

import net.minidev.json.JSONArray;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    @Autowired
    UserService userService;
    
    @Autowired
    LinkedOrderService linkedOrderService;

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
    
    @Autowired
    MailAsyncComponent mailAsyncComponent;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderId) {
    	Order order = orderService.findByOrderId(orderId);
    	// JUST FOR TESTING OF THE EMAIL
//    	Map<String, Object> model = new HashMap<String, Object>();
//        model.put("status", order.getStatus());
//        model.put("orderItems", order.getOrderItems());
//        model.put("date", order.getDate());
//        model.put("id", order.getId());
//    	mailAsyncComponent.sendOrderMail(order.getBuyerid().getEmail(), "Order Placed", "ownOrderEmailTemplate", model);
    	
    	
    	return ResponseEntity.status(HttpStatus.OK).body(order);
    }
    
    @PostMapping("/order/place")
    public ResponseEntity<Order> placeOrder(@RequestBody JSONObject requestBody) {
        
        int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        if (!requestBody.containsKey("selfPick")) {
            System.out.println("Invalid or missing paramters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        User currentUserObject = userService.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", user_id));
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
        boolean selfPick = (boolean) requestBody.get("selfPick");
        if (selfPick) {
            pickedBy = currentUserObject;
        }
        else{
            //not a selfPick, decrease rating by one
            int newRating = currentUserObject.getRating()-1;
            currentUserObject.setRating(newRating);
            userService.save(currentUserObject);
            }
        User buyer = currentUserObject;

        Order order = new Order(pool, date, status, buyer, pickedBy, selfPick);
        Order newOrder = orderService.save(order);

        System.out.println("Order placed");
        System.out.println(newOrder.getId());

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if(cartItems.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        for (CartItem cartItem : cartItems) {
            OrderItems orderItem = new OrderItems(cartItem.getProduct(), newOrder, cartItem.getQuantity(), cartItem.getPrice());
            
            System.out.println("Moving item");
            orderItemsService.save(orderItem);
            cartItemService.delete(cartItem);
        }
        newOrder = orderService.findByOrderId(newOrder.getId());
        System.out.println("Items moved to the order items table");

        

        System.out.println("Items removed from cart items");
        return ResponseEntity.status(HttpStatus.OK).body(newOrder);

    }

    @PostMapping("/order/addLinkedOrders")
    public ResponseEntity<String> addLinkedOrders(@RequestBody JSONObject requestBody) {
        
        int numberOfRecords = Integer.parseInt((String)requestBody.get("nooforders"));
        int storeid = Integer.parseInt((String)requestBody.get("storeid"));
        
        int userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User currentUser = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Pool pool = findPoolByUser(currentUser);
        List<Order> parentOrder = orderService.findByBuyerId(userId);

        if (null != pool) {
            //List<OrderItems> orderItems = orderItemsService.getOrderItemsByStoreid(storeid);
            //List<OrderItems> filteredOrderItems = orderItems.stream()
//                                                    .filter(item -> item.getOrder().getBuyerid().getId() != userId && item.getOrder().getStatus().equals("PLACED"))
//                                                    .sorted(Comparator.comparing(i -> i.getOrder().getDate()))
//                                                    .limit(numberOfRecords)
//                                                    .collect(Collectors.toList());
            List<Order> poolOrders = orderService.getOrdersByPool(pool);
            List<Order> filteredPoolOrders =
                    poolOrders.stream().filter(order ->
                            order.getBuyerid().getId() != userId && order.getStatus().equals("PLACED"))
                            .sorted(Comparator.comparing(Order::getDate))
                            .collect(Collectors.toList());
            //.limit(numberOfRecords)
            System.out.println(filteredPoolOrders);
            
            for(Order parent: parentOrder) {
                int count = 0;
                for(Order order: filteredPoolOrders) {
                    List<OrderItems> filteredOrderItems = orderItemsService.getOrderItemsByOrderId(order.getId());
                    if(filteredOrderItems.size() == 0) {
                        continue;
                    }
                    if(filteredOrderItems.get(0).getProduct().getStoreid() == storeid && count < numberOfRecords) {
                       
                        linkedOrderService.save(new LinkedOrders(parent.getId(), order));
                    	
                        count++;
                    }
                }
            }
            String userEmail = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
            try {
                Map<String, Object> model = new HashMap<String, Object>();
            	    model.put("orders", parentOrder);
            	    mailAsyncComponent.sendOrderMail(userEmail, "Orders to pick up", "ordersToPickUpEmailTemplate", model);
        	    } catch(Exception e) {
        	        System.out.println("Error while sending the email");
        	    }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
    
    @GetMapping("/order/pool_pending/{store_id}")
    public ModelAndView getAllStorePoolProducts(ModelAndView modelAndView,
                                         @PathVariable int store_id) {
        int userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User currentUser = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Pool pool = findPoolByUser(currentUser);
        Map<Integer, JSONArray> map = new HashMap<>();
        if (null != pool) {
            List<Order> poolOrders = orderService.getOrdersByPool(pool);
            List<Order> filteredPoolOrders =
                    poolOrders.stream().filter(order ->
                            order.getBuyerid().getId() != userId 
                            && order.getStatus().equals("PLACED"))
                            .sorted(Comparator.comparing(Order::getDate))
                            .collect(Collectors.toList());
                            //.limit(numberOfRecords)
            System.out.println(filteredPoolOrders);
            
            for(Order order: filteredPoolOrders) {
                List<OrderItems> filteredOrderItems = orderItemsService.getOrderItemsByOrderId(order.getId());
                System.out.println("filtered: "+filteredOrderItems);
                if(filteredOrderItems.size() == 0) {
                    continue;
                }
                if(filteredOrderItems.get(0).getProduct().getStoreid() == store_id) {
                    JSONArray array = new JSONArray();
                    for(OrderItems items: filteredOrderItems) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", items.getId());
                        obj.put("orderid", items.getOrder().getId());
                        obj.put("quantity", items.getQty());
                        obj.put("name", items.getProduct().getName());
                        obj.put("sku", items.getProduct().getSku());
                        
                        array.add(obj);
                    }
                    map.put(order.getId(), array);
                }
            }
        }
        System.out.println("Lala: "+map);
        modelAndView.addObject("items", map);
        modelAndView.addObject("storeid", store_id);
        modelAndView.setViewName("addToCart/orderList");
        return modelAndView;
    }

    private Pool findPoolByUser(User currentUser) {
        Pool pool = poolService.findByLeader(currentUser);

        if (pool == null) {
            PoolMembership poolMembership = poolMembershipService.findByUser(currentUser.getId());

            if (poolMembership == null) {
                System.out.println("User is not a member of any pool");
                return null;
            }
            pool = poolService.findById(poolMembership.getPool());
        }
        return pool;
    }
}
