package com.cmpe275.CartShare.security;

import com.cmpe275.CartShare.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {
    private final int id;
    private final String email;
    private final String password;

    public String getScreename() {
        return screename;
    }

    public void setScreename(String screename) {
        this.screename = screename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String screename;
    private String nickname;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(int id, String email, String password, Collection<? extends GrantedAuthority> authorities, String screenname, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.screename = screenname;
        this.nickname = nickname;
    }

    public static UserPrincipal create(User user) {
        String role;
        if (user.getType().equals("admin")) {
            role = "ROLE_ADMIN";
        } else {
            role = "ROLE_USER";
        }
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(role));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getScreenname(),
                user.getNickname()
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", screename='" + screename + '\'' +
                ", nickname='" + nickname + '\'' +
                ", authorities=" + authorities +
                ", attributes=" + attributes +
                '}';
    }
}
