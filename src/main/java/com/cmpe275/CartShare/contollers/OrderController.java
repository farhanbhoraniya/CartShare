package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.dao.CartItemRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.*;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        if (!requestBody.containsKey("selfPick")) {
            System.out.println("Invalid or missing paramters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User currentUserObject = userService.findById(46).orElseThrow(() -> new ResourceNotFoundException("User", "Id", 46));
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
        User buyer = currentUserObject;

        Order order = new Order(pool, date, status, buyer, pickedBy, selfPick);
        Order newOrder = orderService.save(order);

        System.out.println("Order placed");
        System.out.println(newOrder.getId());

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        for (CartItem cartItem : cartItems) {
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

    @GetMapping("/order/pool_pending/{numberOfRecords}")
    public ModelAndView getStoreProducts(ModelAndView modelAndView,
                                         @PathVariable int numberOfRecords) {
        int userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User currentUser = userService.findById(39).orElseThrow(() -> new ResourceNotFoundException("User", "Id", 39));

        Pool pool = findPoolByUser(currentUser);

        if (null != pool) {
            List<Order> poolOrders = orderService.getOrdersByPool(pool);
            List<Order> filteredPoolOrders =
                    poolOrders.stream().filter(order ->
                            order.getBuyerid().getId() != 39 && order.getStatus().equals("PLACED"))
                            .sorted(Comparator.comparing(Order::getDate))
                            .limit(numberOfRecords).collect(Collectors.toList());
            System.out.println(filteredPoolOrders);

        }

        //modelAndView.addObject("items", array);
        modelAndView.setViewName("addToCart/viewCart");
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
