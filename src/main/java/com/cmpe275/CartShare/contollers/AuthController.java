package com.cmpe275.CartShare.contollers;

import com.cmpe275.CartShare.async.MailAsyncComponent;
import com.cmpe275.CartShare.dao.ConfirmationTokenRepository;
import com.cmpe275.CartShare.exception.BadRequestException;
import com.cmpe275.CartShare.model.AuthProvider;
import com.cmpe275.CartShare.model.ConfirmationToken;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.payload.SignUpRequest;
import com.cmpe275.CartShare.security.TokenProvider;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final UserService userService;

    private final MailAsyncComponent mailAsyncComponent;

    private final TokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenRepository confirmationTokenRepository,
                          UserService userService, MailAsyncComponent mailAsyncComponent,
                          TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userService = userService;
        this.mailAsyncComponent = mailAsyncComponent;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.userExists(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        if (userService.userNickNameExists(signUpRequest.getNickname())) {
            throw new BadRequestException("Nickname already in use.");
        }

        if (userService.userScreenNameExists(signUpRequest.getScreenname())) {
            throw new BadRequestException("Screen name already in use.");
        }

        String type;
        if (signUpRequest.getEmail().endsWith("@sjsu.edu")) {
            type = "admin";
        } else {
            type = "pooler";
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setNickname(signUpRequest.getNickname());
        user.setScreenname(signUpRequest.getScreenname());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
        user.setType(type);
        user.setProvider(AuthProvider.email);

        User result = userService.save(user).orElseThrow(() -> new DataIntegrityViolationException("User not created"));

        ConfirmationToken confirmationToken = new ConfirmationToken(result);
        confirmationTokenRepository.save(confirmationToken);

        mailAsyncComponent.sendMail(result.getEmail(), confirmationToken);
    }

    @PostMapping("/register/social/update")
    public void registerSocialUpdateUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.userNickNameExists(signUpRequest.getNickname())) {
            throw new BadRequestException("Nickname already in use.");
        }

        if (userService.userScreenNameExists(signUpRequest.getScreenname())) {
            throw new BadRequestException("Screen name already in use.");
        }

        Integer user_id = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.findById(user_id).get();
//        User user = new User();
//        user.setEmail(signUpRequest.getEmail());
        user.setNickname(signUpRequest.getNickname());
        user.setScreenname(signUpRequest.getScreenname());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
//        user.setType(type);
//        user.setProvider(AuthProvider.email);
        User result = userService.save(user).orElseThrow(() -> new DataIntegrityViolationException("User not created"));

        ConfirmationToken confirmationToken = new ConfirmationToken(result);
        confirmationTokenRepository.save(confirmationToken);
        mailAsyncComponent.sendMail(result.getEmail(), confirmationToken);
    }

}
