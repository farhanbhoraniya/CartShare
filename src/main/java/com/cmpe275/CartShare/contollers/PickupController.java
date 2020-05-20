package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.async.MailAsyncComponent;
import com.cmpe275.CartShare.dao.LinkedOrdersRepository;
import com.cmpe275.CartShare.model.*;
import com.cmpe275.CartShare.service.OrderItemsService;
import com.cmpe275.CartShare.service.OrderService;
import com.cmpe275.CartShare.service.UserAddressService;
import com.cmpe275.CartShare.service.UserService;
import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PickupController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;
    
    @Autowired
    UserAddressService userAddressService;
    

    @Autowired
    MailAsyncComponent mailAsyncComponent;


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
    @GetMapping("/pickup/order/{order_id}")
    public ModelAndView pickupOrderView(ModelAndView modelAndView, @PathVariable(name="order_id") Integer order_id){

        List<LinkedOrders> pooledOrderList = new ArrayList<>();
        JSONObject poolOrdersJSONarray = new JSONObject();
        JSONArray poolOrders = new JSONArray();

        pooledOrderList=linkedOrdersRepository.findAllByParent_id(order_id);
        List<OrderItems> orderItems = orderItemsService.getOrderItemsByOrderId(order_id);

//        Order order = orderService.fin
/*        List<OrderItems> orderItems = orderItemsService.getOrderItems();*/
//        return pooledOrderList;
//        for(LinkedOrders lo : pooledOrderList){
//            if(lo.getPool_order()!=null && lo.getPool_order().getStatus()!=null && lo.getPool_order().getStatus().equals("PLACED")){
//                JSONArray orderItemsPerPoolOrderArray = new JSONArray();
//                List<OrderItems> orderItems =orderItemsService.getOrderItems(lo.getPool_order());
//
//                System.out.println("Order Items:");
//                System.out.println(orderItems);
//
//                for(OrderItems oi : orderItems){
//                    JSONObject orderitemJSON = new JSONObject();
//                    JSONObject product = new JSONObject();
//                    if(oi.getProduct()!=null){
//                        product.put("name",oi.getProduct().getName());
//                        product.put("desc",oi.getProduct().getDescription());
//                        product.put("storeid",oi.getProduct().getStoreid());
//                        product.put("sku",oi.getProduct().getSku());
//                        product.put("unit",oi.getProduct().getUnit());
//                        product.put("brand",oi.getProduct().getBrand());
//                    }
//                    orderitemJSON.put("product",product);
//                    orderitemJSON.put("price",oi.getPrice());
//                    orderitemJSON.put("id",oi.getId());
//
//                    //individual orderitem of an order added to array
//                    orderItemsPerPoolOrderArray.add(orderitemJSON);
//                }
//                //individual order of a pool order added
//                poolOrdersJSONarray.put("order_id", lo.getPool_order().getId());
//                poolOrdersJSONarray.put("order_time", lo.getPool_order().getDate());
//                poolOrdersJSONarray.put("order_items", orderItemsPerPoolOrderArray);
//
//                poolOrders.add(poolOrdersJSONarray);
//            }
//
//        }
        modelAndView.addObject("orderId", order_id);
        modelAndView.addObject("pooledOrderList", pooledOrderList);
        modelAndView.addObject("orderItems", orderItems);
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

    @PostMapping("/pickup/checkout/{order_id}")
    public JSONObject checkoutOrder(@PathVariable(name="order_id") Integer order_id)
    {
        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//    	int user_id = 5;
        User user = userService.findById(user_id).get();
        List<LinkedOrders> pooledOrderList = linkedOrdersRepository.findAllByParent_id(order_id);
        
        //Updating self pick order
        Order selfOrder = orderService.getOrderByOrderId(order_id);
        selfOrder.setStatus(Order.ORDER_SELF_PICKED);
        selfOrder.setPickedby(user);
        orderService.save(selfOrder);

        List<JSONObject> orderList = new ArrayList<JSONObject>();
        //Updating all order statuses and pickup information
        for (LinkedOrders pooledOrder: pooledOrderList) {
            Order order = pooledOrder.getPool_order();
            order.setStatus(Order.ORDER_PICKED_UP);
            order.setPickedby(user);
            
            orderService.save(order);
            JSONObject temp = new JSONObject();
            temp.put("id", order.getId());
            temp.put("date", order.getDate());
            temp.put("status", order.getStatus());
            temp.put("buyerid", order.getBuyerid());
            UserAddress address = userAddressService.findByUser(order.getBuyerid());
            String addressString = address.getStreetno() + " " + address.getStreetname()+ " " 
            						+ address.getCity() + " " + address.getState() + " " + address.getZip();
            System.out.println(addressString);
            temp.put("address", addressString);
            temp.put("orderItems", order.getOrderItems());
            System.out.println(temp);
            orderList.add(temp);
            Map<String, Object> model = new HashMap<String, Object>();
        	model.put("status", order.getStatus());
        	model.put("orderItems", order.getOrderItems());
        	model.put("date", order.getDate());
        	model.put("id", order.getId());
        	model.put("pickedBy", order.getPickedby().getScreenname());
        	mailAsyncComponent.sendOrderMail(order.getBuyerid().getEmail(), "Order Picked Up!!!", "orderPickedUpEmailTemplate", model);
        }

	      try {
	    	System.out.println(pooledOrderList);
	    	Map<String, Object> model = new HashMap<String, Object>();
	    	model.put("orders", orderList);
	    	mailAsyncComponent.sendOrderMail(selfOrder.getBuyerid().getEmail(), "Orders to Deliver", "orderDeliveryDetailsEmailTemplate", model);
	
	    	
		} catch(Exception e) {
			System.out.println("Error while sending the email");
		}

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message","Order checked out successfully.");

        return response;
    }
}