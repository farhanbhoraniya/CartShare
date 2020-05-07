package com.cmpe275.CartShare.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DeliveryController {

    @GetMapping("/delivery/tasks")
    public ModelAndView getOrdersView(ModelAndView modelAndView) {

        modelAndView.setViewName("delivery_task/index");
        return modelAndView;
    }
}
