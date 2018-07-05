package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uatransport.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
