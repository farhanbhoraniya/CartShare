package com.cmpe275.CartShare.contollers;

import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.service.StoreService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StoreController {

    @Autowired
    StoreService storeService;

    @GetMapping("/storeList")
    public ModelAndView getStoresList(ModelAndView modelAndView)
    {
        List<Store> stores = storeService.findAll();
        modelAndView.setViewName("store/index");
        modelAndView.addObject("stores", stores);
        System.out.println(stores.toString());
        return modelAndView;
    }

    @GetMapping("/store/{store_id}/edit")
    public ModelAndView getEditView(ModelAndView modelAndView, @PathVariable(name = "store_id") Integer store_id) {

        Store store = storeService.findById(store_id);
        modelAndView.addObject("store", store);
        modelAndView.setViewName("store/edit");
        return  modelAndView;
    }

    @GetMapping("/store/{store_id}/view")
    public ModelAndView getView(ModelAndView modelAndView, @PathVariable(name = "store_id") Integer store_id) {

        Store store = storeService.findById(store_id);
        modelAndView.addObject("store", store);
        modelAndView.setViewName("store/view");
        return  modelAndView;
    }

    @GetMapping("/stores")
    public @ResponseBody ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(stores);
    }

    @GetMapping("/store/{store_id}")
    public @ResponseBody ResponseEntity<Store> getStore(@PathVariable int store_id) {
        Store store = storeService.findById(store_id);

        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @PostMapping("/admin/store")
    public @ResponseBody ResponseEntity<Store> createStore(@RequestBody JSONObject storeObject) {

        if (! (storeObject.containsKey("name") && storeObject.containsKey("streetname") && 
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
        } catch(DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newStore);
    }

    @PutMapping("/admin/store/{store_id}")
    public @ResponseBody ResponseEntity<Store> updateStore(@RequestBody JSONObject storeObject, @PathVariable int store_id) {
        Store oldStoreObject = storeService.findById(store_id);

        if (oldStoreObject == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (storeObject.containsKey("name")) {
            oldStoreObject.setName((String)storeObject.get("name"));
        }

        if (storeObject.containsKey("streetname")) {
            oldStoreObject.setStreetname((String)storeObject.get("streetname"));
        }

        if (storeObject.containsKey("streetnumber")) {
            oldStoreObject.setStreetnumber((String)storeObject.get("streetnumber"));
        }

        if (storeObject.containsKey("city")) {
            oldStoreObject.setCity((String)storeObject.get("city"));
        }

        if (storeObject.containsKey("zip")) {
            oldStoreObject.setZip((String)storeObject.get("zip"));
        }

        Store udpatedStore = storeService.update(oldStoreObject);

        return ResponseEntity.status(HttpStatus.OK).body(udpatedStore);

    }

    @DeleteMapping("/admin/store/{store_id}")
    public @ResponseBody ResponseEntity<Store> delteStore(@PathVariable int store_id) {
        Store storeObject = storeService.findById(store_id);

        if (storeObject == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //##############################
        //##############################
        //##############################
        // TODO CHECK FOR PENDING ORDERS
        // TODO DELETE STORE PRODUCTS
        //##############################
        //##############################
        //##############################

        storeService.delete(store_id);

        return ResponseEntity.status(HttpStatus.OK).body(storeObject);
    }
}