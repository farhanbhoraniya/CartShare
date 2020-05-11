package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.dao.LinkedOrdersRepository;
import com.cmpe275.CartShare.model.*;
import com.cmpe275.CartShare.service.OrderItemsService;
import com.cmpe275.CartShare.service.OrderService;
import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PickupController {

    @Autowired
    OrderService orderService;



//    public ModelAndView pickupListView(ModelAndView modelAndView) {
//
//        modelAndView.setViewName("pickup/index");
//        return modelAndView;
//    }

    @Autowired
    LinkedOrdersRepository linkedOrdersRepository;
    @Autowired
    OrderItemsService orderItemsService;

    /*
    * return an array of arrays
    * array of pooledOrders, each contains array of ordered item in that order
    * */
    @GetMapping("/getLinkedPickupOrders/{order_id}")
    public ModelAndView pickupOrderView(ModelAndView modelAndView, @PathVariable(name="order_id") Integer order_id){

        List<LinkedOrders> pooledOrderList = new ArrayList<>();
        JSONArray poolOrdersJSONarray = new JSONArray();

        pooledOrderList=linkedOrdersRepository.findAllByParent_id(order_id);

        for(LinkedOrders lo : pooledOrderList){
            if(lo.getPool_order()!=null && lo.getPool_order().getStatus()!=null && lo.getPool_order().getStatus().equals("PLACED")){
                JSONArray orderItemsPerPoolOrderArray = new JSONArray();
                List<OrderItems> orderItems =orderItemsService.getOrderItems(lo.getPool_order());

                for(OrderItems oi : orderItems){
                    JSONObject orderitemJSON = new JSONObject();
                    JSONObject product = new JSONObject();
                    if(oi.getProduct()!=null){
                        product.put("name",oi.getProduct().getName());
                        product.put("desc",oi.getProduct().getDescription());
                        product.put("storeid",oi.getProduct().getStoreid());
                        product.put("sku",oi.getProduct().getSku());
                        product.put("unit",oi.getProduct().getUnit());
                        product.put("brand",oi.getProduct().getBrand());
                    }
                    orderitemJSON.put("product",product);
                    orderitemJSON.put("price",oi.getPrice());
                    orderitemJSON.put("id",oi.getId());

                    //individual orderitem of an order added to array
                    orderItemsPerPoolOrderArray.add(orderitemJSON);
                }
                //individual order of a pool order added
                poolOrdersJSONarray.add(orderItemsPerPoolOrderArray);
            }

        }
        modelAndView.addObject("poolOrderList", poolOrdersJSONarray);
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