package com.cmpe275.CartShare.WebController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/createStore")
    public String main(Model model) {
        return "createStore"; //view
    }
    
    @GetMapping("/searchStore")
    public String searchStore(Model model) {
        return "searchStore"; //view
    }
    
    @GetMapping("/createProduct")
    public String createProduct(Model model) {
        return "createProduct"; //view
    }
    
    @GetMapping("/searchProduct")
    public String searchProduct(Model model) {
        return "searchProduct"; //view
    }
    
    @GetMapping("/storeDetails")
    public String storeDetails(Model model) {
        return "storeDetails"; //view
    }
    
    @GetMapping("/editStoreDetails")
    public String editStoreDetails(Model model) {
        return "editStoreDetails"; //view
    }
    
    @GetMapping("/productDetails")
    public String productDetails(Model model) {
        return "productDetails"; //view
    }
    
    @GetMapping("/moreDetailsPage")
    public String moreDetailsPage(Model model) {
        return "moreDetailsPage"; //view
    }
    
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        return "sign-up"; //view
    }
    
    @GetMapping("/verification")
    public String verification(Model model) {
        return "verification"; //view
    }
}
