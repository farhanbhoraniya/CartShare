package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    ConfirmationToken findByConfirmationtoken(String confirmationToken);

    ConfirmationToken save(ConfirmationToken confirmationToken);

    void delete(ConfirmationToken confirmationToken);

}
