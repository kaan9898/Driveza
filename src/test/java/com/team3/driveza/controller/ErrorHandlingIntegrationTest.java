package com.team3.driveza.controller;

import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;
import com.team3.driveza.repository.UserRepository;
import com.team3.driveza.repository.VehicleModelRepository;
import com.team3.driveza.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.security.oauth2.client.registration.google.client-id=dummy",
        "spring.security.oauth2.client.registration.google.client-secret=dummy"
})
class ErrorHandlingIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        vehicleRepository.deleteAll();
        vehicleModelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void missingVehicleRendersErrorPage() throws Exception {
        // should go through ControllerAdvice and show the shared 404 error page when vehicle is gone
        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser
    void rentalReturnWithoutLatitudeReturnsBadRequest() throws Exception {
        // leaving out latitude turns into MissingServletRequestParameterException -> 400 page
        mockMvc.perform(post("/rentals/1/return")
                        .param("longitude", "23.0")
                        .param("userId", "1")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser
    void rentingUnavailableVehicleShowsConflictPage() throws Exception {
        // renting a vehicle already RETNED lets the ConflictException path show the 409 view
        VehicleModel model = new VehicleModel();
        model.setBrand("Test");
        model.setModel("X");
        model = vehicleModelRepository.save(model);

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(model);
        vehicle.setLatitude(10.0);
        vehicle.setLongitude(10.0);
        vehicle.setPricePerMin(1.0);
        vehicle.setType(VehicleType.CAR);
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicle = vehicleRepository.save(vehicle);

        User user = new User();
        user.setName("Tester");
        user.setEmail("tester@example.com");
        user.setPassword("secrets");
        user.setRole(Role.USER);
        user.setDob(ZonedDateTime.now().minusYears(25));
        user = userRepository.save(user);

        mockMvc.perform(post("/rentals/rent")
                        .param("vehicleId", String.valueOf(vehicle.getId()))
                        .param("userId", String.valueOf(user.getId()))
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void forbiddenAccessUsesErrorView() throws Exception {
        // security forwards unauthorized access to /403, which now renders the shared error template
        mockMvc.perform(get("/admin/vehicles").with(SecurityMockMvcRequestPostProcessors.user("customer").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

}
