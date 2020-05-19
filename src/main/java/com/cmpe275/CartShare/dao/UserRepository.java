package com.cmpe275.CartShare.dao;

import com.cmpe275.CartShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    public User findByScreenname(String screenName);

    Boolean existsByEmail(String email);

    List<User> findByType(String type);
}
