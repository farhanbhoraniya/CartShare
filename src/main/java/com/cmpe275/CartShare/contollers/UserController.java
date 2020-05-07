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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/oauth2/callback/{registrationId}")
    public void callback(@PathVariable String registrationId) {
        System.out.println(registrationId);
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
