package com.team3.driveza.repository;

import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndDisabledFalse(String email);

    boolean existsByEmail(String email);

    long count();
    long countByRole(Role role);
}