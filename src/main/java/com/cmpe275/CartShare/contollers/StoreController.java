package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.ProductService;
import com.cmpe275.CartShare.service.StoreService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StoreController {

    @Autowired
    StoreService storeService;

    @Autowired
    ProductService productService;

    @GetMapping("/stores")
    public @ResponseBody
    ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(stores);
    }

    @GetMapping("/store/{store_id}")
    public @ResponseBody
    ResponseEntity<Store> getStore(@PathVariable int store_id) {
        Store store = storeService.findById(store_id);

        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @DeleteMapping("/admin/store/{store_id}")
    public @ResponseBody
    ResponseEntity<Store> delteStore(@PathVariable int store_id) {
        Store storeObject = storeService.findById(store_id);

        if (storeObject == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            productService.deleteByStore(storeObject.getId());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error while deleteing some products. Please make sure there are no pending orders.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            storeService.delete(store_id);
        } catch (Exception e) {
            System.out.println("Can not delete store. Please make sure all products are deleted first");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        return ResponseEntity.status(HttpStatus.OK).body(storeObject);
    }

    @GetMapping("/storeList")
    public ModelAndView getStoresList(ModelAndView modelAndView) {
        List<Store> stores = storeService.findAll();
        modelAndView.setViewName("store/index");
        modelAndView.addObject("stores", stores);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return modelAndView;
    }

    @GetMapping("/userStoreList")
    public ModelAndView getUserStoresList(ModelAndView modelAndView) {
        List<Store> stores = storeService.findAll();
        modelAndView.setViewName("addToCart/index");
        modelAndView.addObject("stores", stores);
        System.out.println(stores.toString());
        return modelAndView;
    }

    @GetMapping("/store/{store_id}/edit")
    public ModelAndView getEditView(ModelAndView modelAndView, @PathVariable(name = "store_id") Integer store_id) {

        Store store = storeService.findById(store_id);
        modelAndView.addObject("store", store);
        modelAndView.setViewName("store/edit");
        return modelAndView;
    }

    @GetMapping("/store/{store_id}/view")
    public ModelAndView getView(ModelAndView modelAndView, @PathVariable(name = "store_id") Integer store_id) {

        Store store = storeService.findById(store_id);
        modelAndView.addObject("store", store);
        modelAndView.setViewName("store/view");
        return modelAndView;
    }

    @PostMapping("/admin/store")
    public @ResponseBody
    ResponseEntity<Store> createStore(@RequestBody JSONObject storeObject) {

        if (!(storeObject.containsKey("name") && storeObject.containsKey("streetname") &&
                storeObject.containsKey("streetnumber") && storeObject.containsKey("city") && storeObject.containsKey("zip")
                && storeObject.containsKey("state"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        String name = (String) storeObject.get("name");
        String streetname = (String) storeObject.get("streetname");
        System.out.print(storeObject.get("streetnumber"));
        String streetnumber = (String) storeObject.get("streetnumber");
        String city = (String) storeObject.get("city");
        String zip = (String) storeObject.get("zip");
        String state = (String) storeObject.get("state");

        Store store = new Store(name, streetname, streetnumber, city, zip, state);
        Store newStore;
        try {
            newStore = storeService.save(store);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newStore);
    }

    @PutMapping("/admin/store/{store_id}")
    public @ResponseBody
    ResponseEntity<Store> updateStore(@RequestBody JSONObject storeObject, @PathVariable int store_id) {
        Store oldStoreObject = storeService.findById(store_id);

        if (oldStoreObject == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (storeObject.containsKey("name")) {
            oldStoreObject.setName((String) storeObject.get("name"));
        }

        if (storeObject.containsKey("streetname")) {
            oldStoreObject.setStreetname((String) storeObject.get("streetname"));
        }

        if (storeObject.containsKey("streetnumber")) {
            oldStoreObject.setStreetnumber((String) storeObject.get("streetnumber"));
        }

        if (storeObject.containsKey("city")) {
            oldStoreObject.setCity((String) storeObject.get("city"));
        }

        if (storeObject.containsKey("zip")) {
            oldStoreObject.setZip((String) storeObject.get("zip"));
        }

        if (storeObject.containsKey("state")) {
            oldStoreObject.setState((String) storeObject.get("state"));
        }

        Store udpatedStore = storeService.update(oldStoreObject);

        return ResponseEntity.status(HttpStatus.OK).body(udpatedStore);

    }

}

