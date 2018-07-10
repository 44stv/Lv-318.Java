package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.uatransport.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    UserDetails getUserDetailsByEmail(String userEmail);
    boolean existsUserByEmail(String email);
}
