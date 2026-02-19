package com.team3.driveza.repository;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findAllByStatus(VehicleStatus vehicleStatus, Pageable pageable);

    // Search by brand or model (requires Vehicle has model -> VehicleModel relation)
    @Query(value = """
                select v.*
                from Vehicle v
                join vehicle_model vm
                on v.model_id=vm.id
                where v.status='AVAILABLE'
                  and ( lower(vm.brand) like lower(concat('%', :q, '%'))
                     or lower(vm.model) like lower(concat('%', :q, '%')))
            """, nativeQuery = true)
    Page<Vehicle> searchAvailable(@Param("q") String q,
                                  Pageable pageable);

    // Radius filter (native): spherical law of cosines
    @Query(value = """
                    select * from (
                        select *, acos(
                            sin(radians(v.latitude)) * sin(radians(:lat))
                            + cos(radians(v.latitude)) * cos(radians(:lat))
                            * cos(radians(:lon) - radians(v.longitude))
                        ) * 6371.0 as distance
                        from vehicle v
                        where v.status=:status
                    ) as g
                    where g.distance<:radius
            """, nativeQuery = true)
    Page<Vehicle> findAllWithinRadius(@Param("lat") double lat,
                                      @Param("lon") double lon,
                                      @Param("radius") double radius,
                                      @Param("status") String status,
                                      Pageable pageable);

    @Query(value = """
                    select * from (
                        select v.*, acos(
                            sin(radians(v.latitude)) * sin(radians(:lat))
                            + cos(radians(v.latitude)) * cos(radians(:lat))
                            * cos(radians(:lon) - radians(v.longitude))
                        ) * 6371.0 as distance
                        from vehicle v
                        join vehicle_model vm
                        on v.model_id=vm.id
                        where v.status=:status
                          and ( lower(vm.brand) like lower(concat('%', :q, '%'))
                             or lower(vm.model) like lower(concat('%', :q, '%')))
                    ) as g
                    where g.distance<:radius
            """, nativeQuery = true)
    Page<Vehicle> findAllWithinRadiusAndName(@Param("lat") double lat,
                                             @Param("lon") double lon,
                                             @Param("radius") double radius,
                                             @Param("status") String status,
                                             @Param("q") String q,
                                             Pageable pageable
    );

    long count();

    long countByStatus(VehicleStatus status);
}
