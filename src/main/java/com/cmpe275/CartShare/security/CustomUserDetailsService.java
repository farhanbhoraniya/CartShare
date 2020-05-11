package com.cmpe275.CartShare.security;

import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.exception.ResourceNotFoundException;
import com.cmpe275.CartShare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //private final AuthenticationManager authenticationManager;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        UserPrincipal userDetails = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        //authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        System.out.println(usernamePasswordAuthenticationToken.isAuthenticated());
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //LOGGER.debug(String.format("Auto login %s successfully!", email));
        }

        return userDetails;
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}