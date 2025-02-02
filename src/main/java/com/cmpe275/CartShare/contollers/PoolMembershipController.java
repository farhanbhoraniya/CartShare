package com.cmpe275.CartShare.contollers;

import java.net.InetAddress;
import java.util.List;

import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.security.UserPrincipal;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.dao.ConfirmationTokenRepository;
import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.model.ConfirmationToken;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.EmailServiceImpl;
import com.cmpe275.CartShare.service.PoolMembershipService;
import com.cmpe275.CartShare.service.PoolService;
import com.cmpe275.CartShare.service.UserService;

@RestController
public class PoolMembershipController {
	@Autowired
	PoolRepository poolRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PoolMembershipService poolMembershipService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	EmailServiceImpl emailService;
	
	@Autowired
	Environment env;
	
	@PostMapping("/pool/join")
	public @ResponseBody ResponseEntity<PoolMembership> joinPool(@RequestBody JSONObject poolMember) {

//        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal);
//        User currentUserObject = userService.findByEmail(principal.getUsername());
		int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		System.out.println("Logged in user_id: " + user_id);

		poolMember.put("user", user_id);
//        poolMember.put("user", currentUserObject.getId());

		if (! (poolMember.containsKey("pool") && poolMember.containsKey("user"))) {
			System.out.println("Invalid or missing parameters");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		String reference = null;
		if (poolMember.containsKey("reference")) {
			reference = (String) poolMember.get("reference");			
		} else if(!poolMember.containsKey("knowsLeader") || !(boolean)poolMember.get("knowsLeader")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		String pool;
		int user;
		
		try {
			pool = (String) poolMember.get("pool");
			user_id = (int) poolMember.get("user");
//			System.out.println(user);
		} catch(Exception e) {
			System.out.println("Invalid or missing parametrers");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		Pool poolObject = poolRepository.findById(pool);
		
		if (poolObject == null) {
			System.out.println("Pool does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		if (poolMember.containsKey("knowsLeader")  && (boolean) poolMember.get("knowsLeader")) {
			reference = poolObject.getLeader().getScreenname();
		}


		int finalUser_id = user_id;
		User userObject = userRepository.findById(user_id).orElseThrow(()-> new ResourceNotFoundException("User","user_id", finalUser_id));
		
		if (userObject == null) {
			System.out.println("User does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		PoolMembership userPool = poolMembershipService.findByUser(userObject);
		
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
		
		PoolMembership referenceMembershipCheck = poolMembershipService.membershipCheck(pool, referenceUser);
		
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
		
		PoolMembership newPoolMembership = new PoolMembership(pool, userObject, referenceId, false, false);
		
		try {
			poolMembershipService.save(newPoolMembership);
		} catch (Exception e) {
			System.out.println("Error while adding membership");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		ConfirmationToken confirmationToken = new ConfirmationToken(userObject);
        confirmationTokenRepository.save(confirmationToken);

        try {
//            String ip = InetAddress.getLoopbackAddress().getHostAddress();
//            String port = env.getProperty("server.port");
//            String confirmationURL = "http://" + ip + ":" + port + "/confirm-pool-join?token=" + confirmationToken.getConfirmationtoken();
        	String ip = env.getProperty("aws.server");
        	String confirmationURL = ip + "/confirm-pool-join?token=" + confirmationToken.getConfirmationtoken();
            String subject = "Pool join member";
            String body = "User " + userObject.getScreenname() + " has given you reference to join pool " + poolObject.getName() 
            + ". To confirm user reference click " + confirmationURL;

            emailService.sendEmail(referenceUser.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println("Error while sending the confirmation email");
        }
		
		return ResponseEntity.status(HttpStatus.OK).body(newPoolMembership);	
	}
	
	@GetMapping(value="/confirm-pool-join")
    public @ResponseBody ResponseEntity confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
    	System.out.println("Confirm Token:" + confirmationToken);
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationtoken(confirmationToken);
        PoolMembership poolMembership;
        if(token != null)
        {
        	System.out.println(token);
            User user = token.getUser();
            poolMembership = poolMembershipService.findByUser(user);
            poolMembership.setVerified(true);
            poolMembershipService.save(poolMembership);
            confirmationTokenRepository.delete(token);
        } else {
        	System.out.println("Invalid or missing token");
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        String poolId = poolMembership.getPool();
        Pool pool = poolRepository.findById(poolId);
        ConfirmationToken confirmationTokenApproval = new ConfirmationToken(token.getUser());
        confirmationTokenRepository.save(confirmationTokenApproval);
        
        try {
//            String ip = InetAddress.getLoopbackAddress().getHostAddress();
//            String port = env.getProperty("server.port");
//            String confirmationURL = "http://" + ip + ":" + port + "/leader-approval?token=" + confirmationTokenApproval.getConfirmationtoken();
        	String ip = env.getProperty("aws.server");
        	String confirmationURL = ip + "/leader-approval?token=" + confirmationTokenApproval.getConfirmationtoken();
            String subject = "Pool join member approval";
            String body = "Reference verified. Please approve the membership of the User " + token.getUser().getScreenname() + " for pool " + pool.getName() 
            + ". To approve click " + confirmationURL;
            String email = pool.getLeader().getEmail();

            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            System.out.println("Error while sending the confirmation email");
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

	@GetMapping(value="/leader-approval")
	public void leaderApproval(@RequestParam("token")String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationtoken(confirmationToken);
		PoolMembership poolMembership;
        if(token != null)
        {
            User user = token.getUser();
            poolMembership = poolMembershipService.findByUser(user);
            poolMembership.setLeaderapproved(true);
            poolMembershipService.save(poolMembership);
            confirmationTokenRepository.delete(token);
        } else {
        	System.out.println("Invalid or missing token");
        	return;
        }
	}
	
	
	@DeleteMapping("/pool/{poolId}/leave/{userId}")
	public @ResponseBody ResponseEntity<PoolMembership> leavePool(@PathVariable String poolId, @PathVariable int userId) {
		
		Pool poolObject = poolRepository.findById(poolId);
		
		if (poolObject == null) {
			System.out.println("Pool does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		User userObject = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","Id", userId));
		
		if (userObject == null) {
			System.out.println("User does not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		if (poolObject.getLeader().getId() == userObject.getId()) {
			System.out.println("Leader can not leave the pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		PoolMembership referenceMembershipCheck = poolMembershipService.membershipCheck(poolId, userObject);
		
		if (referenceMembershipCheck == null) {
			System.out.println("User is not a member of the pool");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		try {
			poolMembershipService.delete(userObject);
		} catch (Exception e) {
			System.out.println("Error while deleting the membership");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(referenceMembershipCheck);
	}

	@DeleteMapping("/poolmembership/reference/{request_id}/reject")
	public JSONObject rejectSupport(@PathVariable(name="request_id") int request_id)
	{
		JSONObject response = new JSONObject();
		try{
			poolMembershipService.deleteById(request_id);
			response.put("status", true);
			response.put("message", "Request rejected successfully");

		}catch (Exception e)
		{
			response.put("status", true);
			response.put("message", "Error rejecting request. Please try again.");
		}
		return response;
	}

	@PostMapping("/poolmembership/reference/{request_id}/approve")
	public JSONObject approveSupport(@PathVariable(name="request_id") int request_id)
	{
		JSONObject response = new JSONObject();
		try{

			PoolMembership poolMembership = poolMembershipService.findById(request_id);
			poolMembership.setVerified(true);
			poolMembershipService.save(poolMembership);

			response.put("status", true);
			response.put("message", "Request approved successfully");

		}catch (Exception e)
		{
			response.put("status", true);
			response.put("message", "Error approving request. Please try again.");
		}
		return response;
	}

	@PostMapping("/poolmembership/leader/{request_id}/approve")
	public JSONObject approvalByLeader(@PathVariable(name="request_id") int request_id)
	{
		JSONObject response = new JSONObject();
		try{

			PoolMembership poolMembership = poolMembershipService.findById(request_id);
			poolMembership.setLeaderapproved(true);
			poolMembershipService.save(poolMembership);

			response.put("status", true);
			response.put("message", "Request approved successfully");

		}catch (Exception e)
		{
			response.put("status", true);
			response.put("message", "Error approving request. Please try again.");
		}
		return response;
	}
}
