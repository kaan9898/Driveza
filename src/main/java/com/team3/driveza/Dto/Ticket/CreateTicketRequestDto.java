package com.team3.driveza.Dto.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequestDto {
    private String title;
    private String description;
}