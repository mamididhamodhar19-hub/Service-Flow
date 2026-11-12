package com.serviceflow.dto;

import com.serviceflow.entity.enums.Priority;
import com.serviceflow.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private String title;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private String categoryName;
    private String customerName;
    private String assignedToName;
    private LocalDateTime slaDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
