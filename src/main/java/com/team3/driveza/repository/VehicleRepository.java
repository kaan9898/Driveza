package com.team3.driveza.repository;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends ListCrudRepository<Vehicle, Long> {
    List<Vehicle> findAllByStatus(VehicleStatus vehicleStatus);

    // oh no bro...
    // Spherical law of cosines, 6371km radius of earth
    @NativeQuery("select l.* from locs l where acos( sin(radians(l.lat))*sin(radians(?1))+cos(radians(l.lat))*cos(radians(?1))*cos(radians(?2)-radians(l.long)) ) * 6371 < ?3")
    List<Vehicle> findAllByFormula(Double lat, Double lon, Double radiusInKM);

    // Search by brand or model (requires Vehicle has model -> VehicleModel relation)
    @Query("""
        select v from Vehicle v
        join v.model m
        where v.status = :status
          and ( lower(m.brand) like lower(concat('%', :q, '%'))
             or lower(m.model) like lower(concat('%', :q, '%')) )
    """)
    List<Vehicle> searchAvailable(@Param("status") VehicleStatus status, @Param("q") String q);

    // Radius filter (native): spherical law of cosines
    @Query(value = """
        select * from vehicle v
        where acos(
            sin(radians(v.latitude)) * sin(radians(:lat))
            + cos(radians(v.latitude)) * cos(radians(:lat))
            * cos(radians(:lon) - radians(v.longitude))
        ) * 6371 < :radius
    """, nativeQuery = true)
    List<Vehicle> findAllWithinRadius(@Param("lat") double lat,
                                      @Param("lon") double lon,
                                      @Param("radius") double radius);

    long count();
    long countByStatus(VehicleStatus status);
}
