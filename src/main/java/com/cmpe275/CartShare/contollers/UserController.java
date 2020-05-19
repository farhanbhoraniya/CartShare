package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.async.MailAsyncComponent;
import com.cmpe275.CartShare.dao.ConfirmationTokenRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.ConfirmationToken;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.payload.ApiResponse;
import com.cmpe275.CartShare.security.CurrentUser;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.SecurityService;
import com.cmpe275.CartShare.service.UserService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
public class UserController {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final MailAsyncComponent mailAsyncComponent;

    private final UserService userService;

    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService
            , ConfirmationTokenRepository confirmationTokenRepository
            , MailAsyncComponent mailAsyncComponent
            , SecurityService securityService) {
        this.userService = userService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mailAsyncComponent = mailAsyncComponent;
        this.securityService = securityService;
    }

    @GetMapping("/admin")
    public void adminAPI() {

    }

    @GetMapping("/pooler")
    public void poolerAPI() {

    }

    /*@PostMapping("/register")
    public @ResponseBody
    ResponseEntity<User> registerUser(@RequestBody JSONObject userObject) {

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
        } catch (DataIntegrityViolationException e) {
            System.out.println("Invalid or missing parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        securityService.autoLogin(user.getEmail(), user.getPassword());

        ConfirmationToken confirmationToken = new ConfirmationToken(newUser);
        confirmationTokenRepository.save(confirmationToken);

        mailAsyncComponent.sendMail(newUser.getEmail(), confirmationToken);

        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }*/
    @GetMapping("/order/getCredit")
    public ResponseEntity<String> getCredit() {
        JSONObject obj = new JSONObject();
        try {
            int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        
            Optional<User> user = userService.findById(user_id);
            int rating = user.get().getRating();
            System.out.println(rating);
            obj.put("status", "Success");
            obj.put("credit", -7);
        }catch(Exception e) {
            obj.put("status", "Failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(obj.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
    }
    
    @PostMapping("/oauth2/callback/{registrationId}")
    public void callback(@PathVariable String registrationId) {
        System.out.println(registrationId);
    }

    @PostMapping("/message/{toUser}")
    public void sendMessage(@PathVariable int toUser, @RequestBody org.json.simple.JSONObject messageBody) {
    	int user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//    	int user_id = 21;
    	System.out.println(messageBody);
    	if (!messageBody.containsKey("message")) {
    		System.out.println("Not able to send message. Invalid request body");
    		return;
    	}
    	
    	User user = userService.findById(user_id).get();
    	
    	String subject = "New message from " + user.getScreenname();
    	String message = (String)messageBody.get("message");
//    	String message = "Hello";
    	Optional<User> toUserOptional = userService.findById(toUser);
    	
    	if (!toUserOptional.isPresent()) {
    		System.out.println("User does not exists");
    		return;
    	}
    	
    	User toUserObject = toUserOptional.get();
    	try {
    		mailAsyncComponent.sendMessage(toUserObject.getEmail(), subject, message);
    	} catch(Exception e) {
    		System.out.println("Internal server error while sending the message");
    	}
    	
    	return;
    }
    
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping(value = "/confirm-account")
    public @ResponseBody
    ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationtoken(confirmationToken);

        if (token != null) {
            Optional<User> verifiedUser;
            User user = userService.findByEmail(token.getUser().getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "confirmationToken", confirmationToken));
            user.setVerified(true);
            verifiedUser = userService.save(user);
            if (verifiedUser.isPresent()) {
                ResponseEntity.ok().body(user);
            }
            confirmationTokenRepository.delete(token);
        }

        return ResponseEntity.badRequest().body(new ApiResponse(false, "Token not valid."));
    }
}
