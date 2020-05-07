package com.cmpe275.CartShare.security.oauth2;

import com.cmpe275.CartShare.async.MailAsyncComponent;
import com.cmpe275.CartShare.dao.ConfirmationTokenRepository;
import com.cmpe275.CartShare.dao.UserRepository;
import com.cmpe275.CartShare.exception.OAuth2AuthenticationProcessingException;
import com.cmpe275.CartShare.model.AuthProvider;
import com.cmpe275.CartShare.model.ConfirmationToken;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.security.UserPrincipal;
import com.cmpe275.CartShare.security.oauth2.user.OAuth2UserInfo;
import com.cmpe275.CartShare.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final MailAsyncComponent mailAsyncComponent;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, MailAsyncComponent mailAsyncComponent) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mailAsyncComponent = mailAsyncComponent;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setScreenname(oAuth2UserInfo.getName());
        user.setNickname(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        if (oAuth2UserInfo.getEmail().endsWith("@sjsu.edu")) {
            user.setType("admin");
        } else {
            user.setType("pooler");
        }
        //user.setImageUrl(oAuth2UserInfo.getImageUrl());
        User result =  userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(result);
        confirmationTokenRepository.save(confirmationToken);

        mailAsyncComponent.sendMail(result.getEmail(), confirmationToken);
        return result;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setScreenname(oAuth2UserInfo.getName());
        //existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}
