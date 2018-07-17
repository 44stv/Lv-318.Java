package org.uatransport.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.uatransport.entity.Role;
import org.uatransport.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    String findProviderByEmail(String email);

    void deleteByEmail(String email);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
