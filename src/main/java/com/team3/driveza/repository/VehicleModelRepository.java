package com.team3.driveza.repository;

import com.team3.driveza.model.VehicleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long> {
}
