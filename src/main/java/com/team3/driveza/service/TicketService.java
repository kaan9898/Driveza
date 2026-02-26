package com.team3.driveza.service;

import com.team3.driveza.Dto.Ticket.AdminResponseDto;
import com.team3.driveza.Dto.Ticket.CreateTicketRequestDto;
import com.team3.driveza.Dto.Ticket.TicketResponseDto;
import com.team3.driveza.config.TicketTokenGenerator;
import com.team3.driveza.exception.TicketNotFoundException;
import com.team3.driveza.model.Ticket;
import com.team3.driveza.model.enums.TicketStatus;
import com.team3.driveza.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketService(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    private String createUniqueToken() {
        String token;
        do {
            token = TicketTokenGenerator.generate();
        } while (ticketRepository.existsByTicketToken(token));
        return token;
    }

    @Transactional
    public TicketResponseDto createTicket(String principalName, CreateTicketRequestDto dto) {

        var user = userService.getUserEntityByEmail(principalName);

        Ticket ticket = Ticket.builder()
                .ticketToken(createUniqueToken())
                .user(user)
                .status(TicketStatus.OPEN)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        return toDto(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDto> getMyTickets(String principalName) {

        var user = userService.getUserEntityByEmail(principalName);

        return ticketRepository.findAllByUser(user)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponseDto getMyTicketByToken(String principalName, String token) {

        var user = userService.getUserEntityByEmail(principalName);

        Ticket ticket = ticketRepository.findByTicketTokenAndUser(token, user)
                .orElseThrow(() -> new TicketNotFoundException(token));

        return toDto(ticket);
    }

    @Transactional
    public TicketResponseDto setAdminResponse(String token, AdminResponseDto dto) {

        Ticket ticket = ticketRepository.findByTicketToken(token)
                .orElseThrow(() -> new TicketNotFoundException(token));

        ticket.setAdminResponse(dto.getAdminResponse());
        ticket.setStatus(TicketStatus.CLOSED);
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponseDto closeTicket(String token) {

        Ticket ticket = ticketRepository.findByTicketToken(token)
                .orElseThrow(() -> new TicketNotFoundException(token));

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());

        return toDto(ticketRepository.save(ticket));
    }

    private TicketResponseDto toDto(Ticket t) {
        return TicketResponseDto.builder()
                .token(t.getTicketToken())
                .status(t.getStatus())
                .title(t.getTitle())
                .description(t.getDescription())
                .adminResponse(t.getAdminResponse())
                .createdAt(t.getCreatedAt())
                .closedAt(t.getClosedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDto> getAllTickets(){
        return ticketRepository.findAll().stream().map(this::toDto).toList();
    }
}