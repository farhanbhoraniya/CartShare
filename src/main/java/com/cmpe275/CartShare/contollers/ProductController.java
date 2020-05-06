package com.cmpe275.CartShare.contollers;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.model.Product;
import com.cmpe275.CartShare.model.Store;
import com.cmpe275.CartShare.service.ProductService;
import com.cmpe275.CartShare.service.StoreService;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    StoreService storeService;

    // Adds same product in multiple stores
    // All stores must be valid
    @PostMapping("/admin/products")
    public @ResponseBody ResponseEntity<List<Product>> addProductsInMultipleStores(@RequestBody JSONObject productObject) {

        if (!(productObject.containsKey("sku") && productObject.containsKey("stores") 
                && productObject.containsKey("name") && productObject.containsKey("unit") 
                && productObject.containsKey("price"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<String> stores;
        try {
            stores = (List<String>) productObject.get("stores");
        } catch(Exception e) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String sku = (String) productObject.get("sku");
        String name = (String) productObject.get("name");
        String description = (String) productObject.get("description");
        String imageurl = (String) productObject.get("imageurl");
        String brand = (String) productObject.get("brand");
        String unit = (String) productObject.get("unit");
        double price = (double) ((Object)productObject.get("price"));

        List<Product> products = new ArrayList<Product>();
        for (String storestr : stores) {
            Integer storeId = Integer.parseInt(storestr);
            Store store = storeService.findById(storeId);
            if (store == null) {
                System.out.println("Store not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Product temp = new Product(sku, storeId, name, description, imageurl, brand, unit, price);
            products.add(temp);

        }

        try {
            productService.saveMultiple(products);
        } catch(DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    // Adds product to single store
    @PostMapping("/admin/store/product")
    public @ResponseBody ResponseEntity<Product> addProduct(@RequestBody JSONObject productObject) {

        if (!(productObject.containsKey("sku") && productObject.containsKey("storeid") 
                && productObject.containsKey("name") && productObject.containsKey("unit") 
                && productObject.containsKey("price"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String sku = (String) productObject.get("sku");
        int storeid = (int) productObject.get("storeid");
        String name = (String) productObject.get("name");
        String description = (String) productObject.get("description");
        String imageurl = (String) productObject.get("imageurl");
        String brand = (String) productObject.get("brand");
        String unit = (String) productObject.get("unit");
        double price = (double) productObject.get("price");

        Store store = storeService.findById(storeid);
        if (store == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Product product = new Product(sku, storeid, name, description, imageurl, brand, unit, price);

        try {
            productService.save(product);
        } catch(DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    // Gets the store products
    @GetMapping("/store/{storeId}/products")
    public @ResponseBody ResponseEntity<List<Product>> getStoreProducts(@PathVariable int storeId) {

        Store store = storeService.findById(storeId);

        if (store == null) {
            System.out.println("Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Product> products = productService.findByStore(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    // Gets product from the store
    @GetMapping("/store/{storeid}/product/{sku}")
    public @ResponseBody ResponseEntity<Product> getProduct(@PathVariable String sku, @PathVariable int storeid) {
        Store store = storeService.findById(storeid);

        if (store == null) {
            System.out.println("Store does not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Product product = productService.findProductInStore(storeid, sku);
        if (product == null) {
            System.out.println("Product in store does not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/product/{sku}")
    public @ResponseBody ResponseEntity<List<Product>> getProductBySKU(@PathVariable String sku) {

        List<Product> products = productService.findByProduct(sku);

        if (products == null) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/product")
    public @ResponseBody ResponseEntity<List<Product>> getProductByName(@RequestParam(value = "name") String name) {
        List<Product> products = productService.findByName(name);

        if (products == null) {
            System.out.println("No products found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/admin/store/{storeid}/product/{sku}")
    public @ResponseBody ResponseEntity<Product> updateProduct(@PathVariable int storeid, @PathVariable String sku, 
            @RequestBody JSONObject productObject) {
        Store store = storeService.findById(storeid);

        if (store == null) {
            System.out.println("Store does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Product product = productService.findProductInStore(storeid, sku);

        if (product == null) {
            System.out.println("Product in this store does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (productObject.containsKey("name")) {
            product.setName((String) productObject.get("name"));
        }

        if (productObject.containsKey("description")) {
            product.setDescription((String) productObject.get("description"));
        }

        if (productObject.containsKey("imageurl")) {
            product.setImageurl((String) productObject.get("imageurl"));
        }

        if (productObject.containsKey("brand")) {
            product.setBrand((String) productObject.get("brand"));
        }

        if (productObject.containsKey("unit")) {
            product.setUnit((String) productObject.get("unit"));
        }

        if (productObject.containsKey("price")) {
            product.setPrice((double) productObject.get("price"));
        }

        try {
            productService.save(product);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/admin/store/{storeid}/product/{sku}")
    public @ResponseBody ResponseEntity<Product> deleteProduct(@PathVariable("storeid") int storeid, @PathVariable("sku") String sku) {

        Store store = storeService.findById(storeid);
        if (store == null) {
            System.out.println("Store does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Product product = productService.findProductInStore(storeid, sku);

        if (product == null) {
            System.out.println("Product in this store does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // #############################
        // #############################
        // #############################
        // TODO Check for pending orders
        // #############################
        // #############################
        // #############################

        productService.delete(storeid, sku);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
