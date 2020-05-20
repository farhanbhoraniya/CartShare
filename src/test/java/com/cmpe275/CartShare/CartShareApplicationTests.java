package com.cmpe275.CartShare;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cmpe275.CartShare.contollers.AuthController;
import com.cmpe275.CartShare.contollers.CartContoller;
import com.cmpe275.CartShare.contollers.DeliveryController;
import com.cmpe275.CartShare.contollers.MyOrdersController;
import com.cmpe275.CartShare.contollers.OrderController;
import com.cmpe275.CartShare.contollers.PickupController;
import com.cmpe275.CartShare.contollers.PoolController;
import com.cmpe275.CartShare.contollers.PoolMembershipController;
import com.cmpe275.CartShare.contollers.ProductController;
import com.cmpe275.CartShare.contollers.StoreController;
import com.cmpe275.CartShare.contollers.UserAddressController;
import com.cmpe275.CartShare.contollers.UserController;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CartShareApplicationTests {
    
    @Autowired
    private UserController userController;
    
    @Autowired
    private AuthController authController;
    
    @Autowired
    private CartContoller cartContoller;
    
    @Autowired
    private DeliveryController deliveryController;
    
    @Autowired
    private MyOrdersController myOrdersController;
    
    @Autowired
    private OrderController orderController;
    
    @Autowired
    private PickupController pickupController;
    
    @Autowired
    private PoolController poolController;
    
    @Autowired
    private PoolMembershipController poolMembershipController;
    
    @Autowired
    private ProductController productController;
    
    @Autowired
    private StoreController storeController;
    
    @Autowired
    private UserAddressController userAddressController;

    @Test
    void authController() throws Exception {
    	assertNotNull(authController);
    }
    
    @Test
    void cartContoller() throws Exception {
    	assertNotNull(cartContoller);
    }
    
    @Test
    void myOrdersController() throws Exception {
    	assertNotNull(myOrdersController);
    }
    
    @Test
    void deliveryController() throws Exception {
    	assertNotNull(deliveryController);
    }
    
    @Test
    void orderController() throws Exception {
    	assertNotNull(orderController);
    }
    
    @Test
    void pickupController() throws Exception {
    	assertNotNull(pickupController);
    }
    
    @Test
    void poolController() throws Exception {
    	assertNotNull(poolController);
    }
    
    @Test
    void poolMembershipController() throws Exception {
    	assertNotNull(poolMembershipController);
    }
    
    @Test
    void productController() throws Exception {
    	assertNotNull(productController);
    }
    
    @Test
    void storeController() throws Exception {
    	assertNotNull(storeController);
    }
    
    @Test
    void userAddressController() throws Exception {
    	assertNotNull(userAddressController);
    }
    
    @Test
    void userController() throws Exception {
    	assertNotNull(userController);
    }

}
