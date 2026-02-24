package com.team3.driveza.controller;

import com.team3.driveza.Dto.Ticket.AdminResponseDto;
import com.team3.driveza.Dto.Ticket.CreateTicketRequestDto;
import com.team3.driveza.Dto.Ticket.TicketResponseDto;
import com.team3.driveza.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponseDto create(@RequestBody CreateTicketRequestDto dto, Principal principal) {
        String name = (principal != null) ? principal.getName() : "anonymous";
        return ticketService.createTicket(name, dto);
    }

    @GetMapping("/me")
    public List<TicketResponseDto> myTickets(Principal principal) {
        String name = (principal != null) ? principal.getName() : "anonymous";
        return ticketService.getMyTickets(name);
    }

    @GetMapping("/me/{token}")
    public TicketResponseDto myTicketByToken(@PathVariable String token, Principal principal) {
        String name = (principal != null) ? principal.getName() : "anonymous";
        return ticketService.getMyTicketByToken(name, token);
    }

    @PutMapping("/admin/{token}/response")
    public TicketResponseDto adminResponse(@PathVariable String token,
                                           @RequestBody AdminResponseDto dto) {
        return ticketService.setAdminResponse(token, dto);
    }

    @PutMapping("/admin/{token}/close")
    public TicketResponseDto close(@PathVariable String token) {
        return ticketService.closeTicket(token);
    }
}