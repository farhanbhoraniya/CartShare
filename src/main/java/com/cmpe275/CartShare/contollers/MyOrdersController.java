package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MyOrdersController {

    @GetMapping("/myOrders")
    public ModelAndView getOrdersView(ModelAndView modelAndView) {


//        User user = userService.findById(userId);
//        List<Order> orders = orderService.getSelfPickOrders(true, user);
//
        modelAndView.setViewName("myOrders/index");
//        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

}
