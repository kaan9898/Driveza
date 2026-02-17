package com.team3.driveza.Dto.Ticket;

import java.util.Date;

public class TicketResponseDto {
    private String id;
    private enum statusEnum {
        OPEN,
        CLOSED
    }
    private statusEnum status;
    private String title;
    private String description;
    private String adminResponse;
    private Date createdAt;
}
