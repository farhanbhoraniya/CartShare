package com.cmpe275.CartShare.WebController;

import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    StoreService storeService;

    @GetMapping("/createStore")
    public String main(Model model) {
        return "store/create"; //view
    }

    @GetMapping("/searchStore")
    public String searchStore(Model model) {
        return "searchStore"; //view
    }

    @GetMapping("/createProduct")
    public String createProduct(Model model) {

        List<Store> storeList = storeService.findAll();
        model.addAttribute("stores", storeList);
        return "product/create"; //view
    }

    @GetMapping("/searchProduct")
    public String searchProduct(Model model) {
        return "searchProduct"; //view
    }

    @GetMapping("/storeDetails")
    public String storeDetails(Model model) {
        return "storeDetails"; //view
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

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "index";
    }

    @GetMapping("/oauth2/code/{registrationId}")
    public String callback(@PathVariable String registrationId, @RequestParam("token") String token, HttpSession session) {
        session.setAttribute("Token", token);
        return "redirect:/storeList";
    }

    @GetMapping("/verification")
    public String verification(Model model) {
        return "verification"; //view
    }

    @GetMapping("/createPool")
    public String createPool(Model model) {
        return "/pool/create"; //view
    }

    @GetMapping("/joinPool")
    public String joinPool(Model model) {
        return "joinPool"; //view
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model) {
        return "addToCart/viewCart"; //view
    }
}