package com.cmpe275.CartShare.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignUpRequest {
    @NotBlank
    private String screenname;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public String getScreenname() {
        return screenname;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
