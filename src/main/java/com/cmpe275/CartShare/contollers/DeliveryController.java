package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.model.UserAddress;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.UserAddressService;
import com.cmpe275.CartShare.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.util.List;

@RestController
public class DeliveryController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    UserAddressService userAddressService;

    @GetMapping("/delivery/tasks")
    public ModelAndView getOrdersView(ModelAndView modelAndView) {

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.findById(user_id).get();
        List<Order> deliveryOrders = orderService.getOrdersPendingDelivery(user);

        modelAndView.addObject("deliveryOrders", deliveryOrders);
        modelAndView.setViewName("delivery_task/index");
        return modelAndView;
    }

    @GetMapping("/delivery/order/{order_id}")
    public ModelAndView getDeliveryOrderDetails(ModelAndView modelAndView, @PathVariable(name="order_id") int orderId){

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Order order = orderService.getOrderByOrderId(orderId);
        UserAddress address = userAddressService.findByUser(order.getBuyerid());

        modelAndView.addObject("order", order);
        modelAndView.addObject("address", address);
        modelAndView.setViewName("delivery_task/view");
        return modelAndView;
    }

    @PostMapping("/delivery/complete/{order_id}")
    public JSONObject completeOrderDelivery(@PathVariable(name="order_id") int orderId){

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Order order = orderService.getOrderByOrderId(orderId);
        order.setStatus(Order.ORDER_DELIVERED);
        orderService.save(order);

        //increase credit
        User deliveredBy = order.getPickedby();
        User orderedBy = order.getBuyerid();
        if(deliveredBy!=null && orderedBy!=null && deliveredBy.getId()!=orderedBy.getId()){
            System.out.println("delivered by :"+deliveredBy.getScreenname());
            System.out.println("delivered by rating:"+deliveredBy.getRating());
            int newRating = deliveredBy.getRating()+1;
            deliveredBy.setRating(newRating);
            userService.save(deliveredBy);

        }

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "Order delivered successfully");

        //TODO: Trigger Email notifications
        return response;
    }

    @PostMapping("/delivery/incomplete/{order_id}")
    public JSONObject orderDeliveryIncomplete(@PathVariable(name="order_id") int orderId){

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Order order = orderService.getOrderByOrderId(orderId);
        order.setStatus(Order.ORDER_PICKED_UP);
        orderService.save(order);
        //decrease ratings
        User deliveredBy = order.getPickedby();
        User orderedBy = order.getBuyerid();
        if(deliveredBy!=null && orderedBy!=null && deliveredBy.getId()!=orderedBy.getId()){
            int newRating = deliveredBy.getRating()-1;
            deliveredBy.setRating(newRating);
            userService.save(deliveredBy);
        }

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "Order status updated successfully");

        //TODO: Trigger Email notifications
        return response;
    }

}