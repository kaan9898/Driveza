package com.team3.driveza.repository;

import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;

public interface VehicleQuery {
    long getId();
    long getModelId();
    String getBrand();
    String getModelName();
    double getLongitude();
    double getLatitude();
    VehicleType getType();
    VehicleStatus getStatus();
    double getPricePerMin();
    // Now the real field
    Double getDistanceKm();
}
