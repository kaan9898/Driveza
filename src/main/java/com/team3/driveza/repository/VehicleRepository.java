package com.team3.driveza.repository;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    Iterable<Vehicle> findAllByStatus(VehicleStatus vehicleStatus);

    // oh no bro...
    // Spherical law of cosines, 6371km radius of earth
    @NativeQuery("select l.* from locs l where acos( sin(radians(l.lat))*sin(radians(?1))+cos(radians(l.lat))*cos(radians(?1))*cos(radians(?2)-radians(l.long)) ) * 6371 < ?3")
    Iterable<Vehicle> findAllByFormula(Double lat, Double lon, Double radiusInKM);
}
