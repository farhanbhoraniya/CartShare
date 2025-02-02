package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.PoolService;
import com.cmpe275.CartShare.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PoolController {

    @Autowired
    PoolService poolService;

    @Autowired
    UserService userService;

    @Autowired
    PoolMembershipRepository poolMembershipRepository;

    @PostMapping("/pool")
    public @ResponseBody
    ResponseEntity<Pool> createPool(@RequestBody JSONObject poolObject) {

        if (!(poolObject.containsKey("id") && poolObject.containsKey("name") && poolObject.containsKey("neighborhood") && poolObject.containsKey("description")
                && poolObject.containsKey("zip"))) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String id = (String) poolObject.get("id");
        String name = (String) poolObject.get("name");
        String neighborhood = (String) poolObject.get("neighborhood");
        String description = (String) poolObject.get("description");
        String zip = (String) poolObject.get("zip");

        int leader = user_id;

        Pool temp = poolService.findByName(name);

        if (temp != null) {
            System.out.println("Pool with same name already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = userService.findById(leader).orElseThrow(() -> new ResourceNotFoundException("User", "Id", leader));

        if (user == null) {
            System.out.println("User does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        PoolMembership userMembershipCheck = poolMembershipRepository.findByUser(user);

        if (userMembershipCheck != null) {
            System.out.println("User is already a member of pool");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Pool poolLeadershipCheck = poolService.findByLeader(user);

        if (poolLeadershipCheck != null) {
            System.out.println("User is already a leader of the pool");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Pool newPoolObject;
        try {
            newPoolObject = new Pool(id, name, neighborhood, description, zip, user);
        } catch (Exception e) {
            System.out.println("Invalid or missing paramters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Pool newPool = poolService.save(newPoolObject);

        return ResponseEntity.status(HttpStatus.OK).body(newPool);
    }

    @GetMapping("/pools")
    public @ResponseBody
    ResponseEntity<List<Pool>> getAllPools() {
    	
        List<Pool> pools = poolService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(pools);
    }

    @GetMapping("/pool")
    public @ResponseBody
    ResponseEntity<List<Pool>> getPool(@RequestParam(required = false, name = "id") String id,
                                       @RequestParam(required = false, name = "name") String name,
                                       @RequestParam(required = false, name = "zip") String zip,
                                       @RequestParam(required = false, name = "neighborhood") String neighborhood) {

        if (id == null && name == null && zip == null && neighborhood == null) {
            System.out.println("One query parameter is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Pool> pools = new ArrayList<Pool>();
        if (id != null) {
            Pool pool = poolService.findById(id);
            pools = new ArrayList<Pool>();
            pools.add(pool);

        } else if (name != null) {
            Pool pool = poolService.findByName(name);
            pools = new ArrayList<Pool>();
            pools.add(pool);

        } else if (zip != null) {
            pools = poolService.findByZip(zip);

        } else if (neighborhood != null) {
            pools = poolService.findByNeighborhood(neighborhood);
        }

        return ResponseEntity.status(HttpStatus.OK).body(pools);
    }

    @PutMapping("/pool/{poolid}")
    public @ResponseBody
    ResponseEntity<Pool> updatePool(@PathVariable String poolid, @RequestBody JSONObject poolObject) {
        Pool pool = poolService.findById(poolid);

        if (pool == null) {
            System.out.println("Pool does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (poolObject.containsKey("name")) {
            String name = (String) poolObject.get("name");
            pool.setName(name);
        }

        if (poolObject.containsKey("neighborhood")) {
            String neighborhood = (String) poolObject.get("neighborhood");
            pool.setNeighborhood(neighborhood);
        }

        if (poolObject.containsKey("description")) {
            String description = (String) poolObject.get("description");
            pool.setDescription(description);
        }

        try {
            poolService.update(pool);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data validation error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(pool);
    }

    @DeleteMapping("/pool/{poolid}")
    public @ResponseBody
    ResponseEntity<Pool> deletePool(@PathVariable String poolid) {

        Pool pool = poolService.findById(poolid);

        if (pool == null) {
            System.out.println("Pool does not exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<PoolMembership> poolMembers = poolMembershipRepository.findByPool(poolid);
        System.out.println(poolMembers);
        if (poolMembers.size() != 0) {
            System.out.println("There are other members in the pool.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        poolService.delete(poolid);
        return ResponseEntity.status(HttpStatus.OK).body(pool);
    }

    @GetMapping("/poolList")
    public ModelAndView getPoolsList(ModelAndView modelAndView) {
        List<Pool> pools = poolService.findAll();

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.findById(user_id).get();
        Pool userPoolInfo = userService.findUserPool(user);


        modelAndView.setViewName("pool/index");
        modelAndView.addObject("user", user);
        modelAndView.addObject("pools", pools);
        modelAndView.addObject("userPool", userPoolInfo);
        System.out.println("Pools: " + pools.toString());
        return modelAndView;
    }

}
