package com.serviceflow.dto;

import com.serviceflow.entity.enums.Priority;
import com.serviceflow.entity.enums.TicketStatus;
import com.serviceflow.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;

    @NotNull
    private Priority priority;

    // For customers only – the backend fills customerId from JWT
}
