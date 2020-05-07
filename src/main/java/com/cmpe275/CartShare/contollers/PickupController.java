package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class PickupController {

    @GetMapping("/pickupList")
    public ModelAndView pickupListView(ModelAndView modelAndView) {

        modelAndView.setViewName("pickup/index");
        return modelAndView;
    }

    @GetMapping("/pickup/order/{order_id}")
    public ModelAndView pickupOrderView(ModelAndView modelAndView, @PathVariable(name="order_id") Integer order_id){

        modelAndView.addObject("order_id", order_id);
        modelAndView.setViewName("pickup/view");
        return modelAndView;
    }

}