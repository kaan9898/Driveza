package com.team3.driveza.repository;

import com.team3.driveza.model.Ticket;
import com.team3.driveza.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketToken(String token);
    List<Ticket> findAllByUser(User user);
    Optional<Ticket> findByTicketTokenAndUser(String token, User user);
    boolean existsByTicketToken(String ticketToken);
}