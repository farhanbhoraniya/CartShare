package com.cmpe275.CartShare.contollers;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.PoolMembershipService;
import com.cmpe275.CartShare.service.PoolService;

@RestController
public class PoolMembershipController {

	@Autowired
	PoolRepository poolRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PoolMembershipService poolMembershipService;
	
	@PostMapping("/pool/join")
	public @ResponseBody ResponseEntity<PoolMembership> joinPool(@RequestBody JSONObject poolMember) {
		
		if (! (poolMember.containsKey("pool") && poolMember.containsKey("user") && poolMember.containsKey("reference"))) {
			System.out.println("Invalid or missing parameters");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		String pool;
		int user;
		String reference;
		
		try {
			pool = (String) poolMember.get("pool");
			user = (int) poolMember.get("user");
			reference = (String) poolMember.get("reference");
		} catch(Exception e) {
			System.out.println("Invalid or missing parametrers");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		Pool poolObject = poolRepository.findById(pool);
		
		if (poolObject == null) {
			System.out.println("Pool does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		User userObject = userRepository.findById(user);
		
		if (userObject == null) {
			System.out.println("User does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		PoolMembership userPool = poolMembershipService.findByUser(user);
		
		if (userPool != null) {
			System.out.println("User is already a member of the one pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		Pool poolByLeader = poolRepository.findByLeader(userObject);
		
		if (poolByLeader != null) {
			System.out.println("User is already a member of the one pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		User referenceUser = userRepository.findByScreenname(reference);
		
		if (referenceUser == null) {
			System.out.println("User with screen name not available");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		PoolMembership referenceMembershipCheck = poolMembershipService.membershipCheck(pool, referenceUser.getId());
		
		if (referenceMembershipCheck == null && referenceUser.getId() != poolObject.getLeader().getId()) {
			System.out.println("Reference user is neither member not leader of the pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		int referenceId;
		if (referenceMembershipCheck == null) {
			referenceId = poolObject.getLeader().getId();
		} else {
			referenceId = referenceUser.getId();
		}
		
		PoolMembership newPoolMembership = new PoolMembership(pool, user, referenceId, false);
		
		try {
			poolMembershipService.save(newPoolMembership);
		} catch (Exception e) {
			System.out.println("Error while adding membership");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(newPoolMembership);	
	}
	
	@DeleteMapping("/pool/{poolId}/leave/{userId}")
	public @ResponseBody ResponseEntity<PoolMembership> leavePool(@PathVariable String poolId, @PathVariable int userId) {
		
		Pool poolObject = poolRepository.findById(poolId);
		
		if (poolObject == null) {
			System.out.println("Pool does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		User userObject = userRepository.findById(userId);
		
		if (userObject == null) {
			System.out.println("User does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		if (poolObject.getLeader().getId() == userObject.getId()) {
			System.out.println("Leader can not leave the pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		PoolMembership referenceMembershipCheck = poolMembershipService.membershipCheck(poolId, userId);
		
		if (referenceMembershipCheck == null) {
			System.out.println("User is not a member of the pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		try {
			poolMembershipService.delete(userId);
		} catch (Exception e) {
			System.out.println("Error while deleting the membership");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(referenceMembershipCheck);
	}
}
