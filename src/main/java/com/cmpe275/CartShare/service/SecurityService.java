package com.cmpe275.CartShare.service;

public interface SecurityService {

    String findLoggedInUser();

    void autoLogin(String email, String password);
}
