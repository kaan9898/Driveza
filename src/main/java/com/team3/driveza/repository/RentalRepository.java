package com.team3.driveza.repository;

import com.team3.driveza.model.Rental;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends ListCrudRepository<Rental, Long> {
}
