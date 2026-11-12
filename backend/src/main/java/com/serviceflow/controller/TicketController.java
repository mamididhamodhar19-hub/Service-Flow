package com.serviceflow.controller;

import com.serviceflow.dto.TicketRequest;
import com.serviceflow.dto.TicketResponse;
import com.serviceflow.entity.enums.Priority;
import com.serviceflow.entity.enums.TicketStatus;
import com.serviceflow.security.CustomUserDetails;
import com.serviceflow.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Endpoints for creating, managing, and tracking service tickets")
@SecurityRequirement(name = "BearerAuth")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @Operation(summary = "Get tickets accessible to current user")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ticketService.getTicketsForUser(userDetails.getId()));
    }

    @PostMapping
    @Operation(summary = "Create a new support ticket (Customer only)")
    public ResponseEntity<TicketResponse> createTicket(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign ticket to support agent (Admin/Support only)")
    public ResponseEntity<TicketResponse> assignTicket(
            @PathVariable Long id,
            @RequestParam Long supportId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, supportId));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update ticket status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.changeStatus(id, status));
    }

    @PutMapping("/{id}/priority")
    @Operation(summary = "Update ticket priority")
    public ResponseEntity<TicketResponse> updatePriority(
            @PathVariable Long id,
            @RequestParam Priority priority) {
        return ResponseEntity.ok(ticketService.updatePriority(id, priority));
    }
}
