package com.team3.driveza.config;

import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;
import com.team3.driveza.repository.UserRepository;
import com.team3.driveza.repository.VehicleRepository;
import com.team3.driveza.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartupDataSeeder implements ApplicationRunner {

    private static final String SEED_PASSWORD_PLAIN = "123";
    private static final double DEFAULT_CENTER_LAT = 56.9496; // same as map.html default (Riga)
    private static final double DEFAULT_CENTER_LON = 24.1052;

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleModelService vehicleModelService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.vehicles.count:15}")
    private int targetVehicleCount;

    @Value("${app.seed.vehicles.maxRadiusKm:50}")
    private int maxRadiusKm;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) return;

        upsertUser("admin@gmail.com", Role.ADMIN);
        upsertUser("user@gmail.com", Role.USER);

        seedVehicles(targetVehicleCount);
    }

    private void upsertUser(String email, Role role) {
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setRole(role);
        user.setDisabled(false);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(randomUsername(role));
        }

        // Keep dev credentials stable on every run.
        user.setPassword(passwordEncoder.encode(SEED_PASSWORD_PLAIN));

        userRepository.save(user);
    }

    private String randomUsername(Role role) {
        String prefix = (role == Role.ADMIN) ? "admin" : "user";
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toLowerCase(Locale.ROOT);
    }

    private void seedVehicles(int targetCount) {
        if (targetCount <= 0) return;

        long existing = vehicleRepository.count();
        int missing = (int) Math.max(0, targetCount - existing);
        if (missing == 0) return;

        List<String[]> models = List.of(
                new String[]{"Toyota", "Corolla"},
                new String[]{"Toyota", "RAV4"},
                new String[]{"Honda", "Civic"},
                new String[]{"Honda", "CR-V"},
                new String[]{"BMW", "3 Series"},
                new String[]{"BMW", "X5"},
                new String[]{"Mercedes-Benz", "C-Class"},
                new String[]{"Mercedes-Benz", "GLE"},
                new String[]{"Volkswagen", "Golf"},
                new String[]{"Volkswagen", "Passat"},
                new String[]{"Audi", "A4"},
                new String[]{"Audi", "Q5"},
                new String[]{"Nissan", "Qashqai"},
                new String[]{"Hyundai", "Tucson"},
                new String[]{"Kia", "Sportage"}
        );

        for (int i = 0; i < missing; i++) {
            String[] pick = models.get(random.nextInt(models.size()));
            VehicleModel vehicleModel = vehicleModelService.findOrCreate(pick[0], pick[1]);

            double[] point = randomPointKm(DEFAULT_CENTER_LAT, DEFAULT_CENTER_LON, Math.max(1, maxRadiusKm));
            Vehicle v = new Vehicle();
            v.setModel(vehicleModel);
            v.setLatitude(point[0]);
            v.setLongitude(point[1]);
            v.setPricePerMin(randomPricePerMin());
            v.setType(randomVehicleType());
            v.setStatus(VehicleStatus.AVAILABLE);

            vehicleRepository.save(v);
        }
    }

    private VehicleType randomVehicleType() {
        int roll = random.nextInt(100);
        if (roll < 75) return VehicleType.CAR;
        if (roll < 90) return VehicleType.PREMIUM_CAR;
        return VehicleType.MOTORCYCLE;
    }

    private double randomPricePerMin() {
        // Keep it reasonable for UI; round to 2 decimals.
        double min = 0.15;
        double max = 1.50;
        double raw = min + (max - min) * random.nextDouble();
        return Math.round(raw * 100.0) / 100.0;
    }

    private double[] randomPointKm(double centerLat, double centerLon, double radiusKm) {
        // Very small-area approximation: good enough for seeding within tens of km.
        double r = radiusKm * Math.sqrt(random.nextDouble());
        double theta = random.nextDouble() * 2.0 * Math.PI;

        double dLat = (r / 111.0) * Math.cos(theta);
        double dLon = (r / (111.0 * Math.cos(Math.toRadians(centerLat)))) * Math.sin(theta);

        return new double[]{centerLat + dLat, centerLon + dLon};
    }
}

