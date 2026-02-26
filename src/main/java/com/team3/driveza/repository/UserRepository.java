package com.team3.driveza.repository;

import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByDisabledFalse(Pageable pageable);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndDisabledFalse(String email);

    boolean existsByEmail(String email);

    long count();
    long countByDisabledFalse();
    long countByRoleAndDisabledFalse(Role role);
}