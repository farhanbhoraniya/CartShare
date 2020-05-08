package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.dao.LinkedOrdersRepository;
import com.cmpe275.CartShare.model.*;
import com.cmpe275.CartShare.service.OrderItemsService;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.UserService;
import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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


    /*@GetMapping("/pickupList")
    public ModelAndView pickupListView(ModelAndView modelAndView) {

        modelAndView.setViewName("pickup/index");
        return modelAndView;
    }*/

    @Autowired
    LinkedOrdersRepository linkedOrdersRepository;
    @Autowired
    OrderItemsService orderItemsService;

    @GetMapping("/pickup/order/{order_id}")
    public ModelAndView pickupOrderView(ModelAndView modelAndView, @PathVariable(name="order_id") Integer order_id){

        List<LinkedOrders> linkedOrders = new ArrayList<>();
        JSONArray poolOrderList = new JSONArray();

        linkedOrders=linkedOrdersRepository.findAllByParent_id(order_id);

        for(LinkedOrders lo : linkedOrders){
            System.out.println(lo.getId());
            JSONObject linkedOrder = new JSONObject();
            if(lo.getPool_order()!=null && lo.getPool_order().getStatus()!=null && lo.getPool_order().getStatus().equals("PLACED")){
                JSONArray orderItemsPerPoolOrder = new JSONArray();

                List<OrderItems> orderItems =orderItemsService.getOrderItems(lo.getPool_order());
                //add orderitems to order
                for(OrderItems oi : orderItems){
                    JSONObject orderitem = new JSONObject();
                    JSONObject product = new JSONObject();
                    if(oi.getProduct()!=null){
                        product.put("name",oi.getProduct().getName());
                        product.put("desc",oi.getProduct().getDescription());
                        product.put("storeid",oi.getProduct().getStoreid());
                        product.put("sku",oi.getProduct().getSku());
                        product.put("unit",oi.getProduct().getUnit());
                        product.put("brand",oi.getProduct().getBrand());
                    }
                    orderitem.put("product",product);
                    orderitem.put("price",oi.getPrice());
                    orderitem.put("id",oi.getId());
                    orderItemsPerPoolOrder.add(orderitem);
                }
                linkedOrder.put("orderitems",orderItemsPerPoolOrder);
                poolOrderList.add(linkedOrder);
            }

        }
        //System.out.println(poolOrderList);

        modelAndView.addObject("poolOrderList", poolOrderList);
        modelAndView.setViewName("pickup/view");
        return modelAndView;
    }


    @GetMapping("/getSelfPickOpenOrders/{userid}")
    public ModelAndView pickupListView(ModelAndView modelAndView,@PathVariable(name="userid") Integer userid) {
        List<Order> selfPickUpOpenOrders = new ArrayList<>();
        selfPickUpOpenOrders=orderService.getOpenOrdersByUserId(userid);

        JSONArray array = new JSONArray();
        for(Order order: selfPickUpOpenOrders) {
            JSONObject obj = new JSONObject();
            obj.put("orderid", order.getId());
            obj.put("date", order.getDate());
            obj.put("status", order.getStatus());

            array.add(obj);
        }
        System.out.print("cartItem: "+ array);
        modelAndView.addObject("selfPickUpOpenOrders", array);
        modelAndView.setViewName("pickup/index");
        return modelAndView;
    }

}