package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.CartItem;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.UserService;
import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PickupController {

    @Autowired
    OrderService orderService;



//    public ModelAndView pickupListView(ModelAndView modelAndView) {
//
//        modelAndView.setViewName("pickup/index");
//        return modelAndView;
//    }

    @GetMapping("/pickup/order/{order_id}")
    public ModelAndView pickupOrderView(ModelAndView modelAndView, @PathVariable(name="order_id") Integer order_id){

        modelAndView.addObject("order_id", order_id);
        modelAndView.setViewName("pickup/view");
        return modelAndView;
    }

    @GetMapping("/pickupList")
    public ModelAndView pickupListView(ModelAndView modelAndView) {

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Order> selfPickUpOpenOrders = new ArrayList<>();
        selfPickUpOpenOrders=orderService.getOpenOrdersByUserId(user_id);

//        JSONArray array = new JSONArray();
//        for(Order order: selfPickUpOpenOrders) {
//            JSONObject obj = new JSONObject();
//            Order order = new Order();
//            order.setId();
//            obj.put("orderid", order.getId());
//            obj.put("date", order.getDate());
//            obj.put("status", order.getStatus());
//
//            array.add(obj);
//        }
//        System.out.print("cartItem: "+ array);
        modelAndView.addObject("selfPickUpOpenOrders", selfPickUpOpenOrders);
        modelAndView.setViewName("pickup/index");
        return modelAndView;
    }
}