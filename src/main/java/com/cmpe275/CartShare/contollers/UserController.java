package com.cmpe275.CartShare.contollers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("/admin")
	public void adminAPI() {
		
	}
	
	@GetMapping("/pooler")
	public void poolerAPI() {
		
	}
	
	@PostMapping("/register")
	public @ResponseBody ResponseEntity<User> registerUser(@RequestBody JSONObject userObject) {
		
		if (!userObject.containsKey("email") || !userObject.containsKey("screenname") || !userObject.containsKey("nickname")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		String email = (String) userObject.get("email");
		String screenname = (String) userObject.get("screenname");
		String nickname = (String) userObject.get("nickname");
		String password = (String) userObject.get("password");
		
		String type;
		if (email.endsWith("@sjsu.edu")) {
			type = "admin";
		} else {
			type = "pooler";
		}
		
		User user = new User(email, screenname, nickname, password, type);
		User newUser;
		try {
			newUser = userService.save(user);
		} catch(DataIntegrityViolationException e) {
			System.out.println("Invalid or missing parameters");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(newUser);
	}
	
	@GetMapping("/login")
    public @ResponseBody ResponseEntity<Object> login() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(principal);
		User newUser = userService.findByEmail(principal.getUsername());
		return ResponseEntity.status(200).body(newUser);
	}
}
