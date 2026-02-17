package com.team3.driveza.repository;

import com.team3.driveza.model.VehicleModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleModelRepository extends ListCrudRepository<VehicleModel, Long> {
}
