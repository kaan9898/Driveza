package com.team3.driveza.Dto.Ticket;

import com.team3.driveza.model.enums.TicketStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponseDto {
    private String token;
    private TicketStatus status;
    private String title;
    private String description;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
}