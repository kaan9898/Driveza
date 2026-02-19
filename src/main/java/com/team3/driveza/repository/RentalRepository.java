package com.team3.driveza.repository;

import com.team3.driveza.model.Rental;
import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.RentalStatus;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends ListCrudRepository<Rental, Long> {
    List<Rental> findByUserId(long userId);
    Optional<Rental> findFirstByUserIdAndStatus(long userId, RentalStatus status);

    long user(User user);
}
