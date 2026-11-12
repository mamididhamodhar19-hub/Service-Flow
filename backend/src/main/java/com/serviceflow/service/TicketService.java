package com.serviceflow.service;

import com.serviceflow.config.AppProperties;
import com.serviceflow.dto.TicketRequest;
import com.serviceflow.dto.TicketResponse;
import com.serviceflow.entity.Ticket;
import com.serviceflow.entity.TicketCategory;
import com.serviceflow.entity.User;
import com.serviceflow.entity.enums.Priority;
import com.serviceflow.entity.enums.Role;
import com.serviceflow.entity.enums.TicketStatus;
import com.serviceflow.exception.BusinessException;
import com.serviceflow.exception.ResourceNotFoundException;
import com.serviceflow.repository.TicketCategoryRepository;
import com.serviceflow.repository.TicketRepository;
import com.serviceflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AppProperties appProperties;

    // Simple thread‑safe ticket number generator – increment per application instance
    private final AtomicLong ticketSeq = new AtomicLong(1);

    private String generateTicketNumber() {
        long seq = ticketSeq.getAndIncrement();
        return String.format("SF-%d-%05d", LocalDateTime.now().getYear(), seq);
    }

    private LocalDateTime calculateSlaDeadline(Priority priority, LocalDateTime createdAt) {
        int hours;
        switch (priority) {
            case LOW: hours = appProperties.getSla().getLow(); break;
            case MEDIUM: hours = appProperties.getSla().getMedium(); break;
            case HIGH: hours = appProperties.getSla().getHigh(); break;
            case CRITICAL: hours = appProperties.getSla().getCritical(); break;
            default: hours = appProperties.getSla().getMedium();
        }
        return createdAt.plusHours(hours);
    }

    @Transactional
    public TicketResponse createTicket(Long customerId, TicketRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (!customer.getRole().equals(Role.ROLE_CUSTOMER)) {
            throw new BusinessException("Only customers can create tickets");
        }
        TicketCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Ticket ticket = new Ticket();
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(request.getPriority());
        ticket.setCustomer(customer);
        ticket.setCategory(category);
        LocalDateTime now = LocalDateTime.now();
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticket.setSlaDeadline(calculateSlaDeadline(request.getPriority(), now));
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = user.getRole();
        List<Ticket> tickets;
        if (role.equals(Role.ROLE_ADMIN)) {
            tickets = ticketRepository.findAll();
        } else if (role.equals(Role.ROLE_SUPPORT)) {
            tickets = ticketRepository.findByAssignedToId(userId);
        } else { // CUSTOMER
            tickets = ticketRepository.findByCustomerId(userId);
        }
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse assignTicket(Long ticketId, Long supportId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User support = userRepository.findById(supportId)
                .orElseThrow(() -> new ResourceNotFoundException("Support user not found"));
        if (!support.getRole().equals(Role.ROLE_SUPPORT) && !support.getRole().equals(Role.ROLE_ADMIN)) {
            throw new BusinessException("Only support or admin can be assigned");
        }
        ticket.setAssignedTo(support);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @Transactional
    public TicketResponse updatePriority(Long ticketId, Priority newPriority) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setPriority(newPriority);
        // Re‑calculate SLA deadline based on creation time
        ticket.setSlaDeadline(calculateSlaDeadline(newPriority, ticket.getCreatedAt()));
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @Transactional
    public TicketResponse changeStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(newStatus);
        if (newStatus == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else if (newStatus == TicketStatus.CLOSED) {
            ticket.setClosedAt(LocalDateTime.now());
        }
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse resp = new TicketResponse();
        resp.setId(ticket.getId());
        resp.setTicketNumber(ticket.getTicketNumber());
        resp.setTitle(ticket.getTitle());
        resp.setDescription(ticket.getDescription());
        resp.setStatus(ticket.getStatus());
        resp.setPriority(ticket.getPriority());
        resp.setCategoryName(ticket.getCategory().getName());
        resp.setCustomerName(ticket.getCustomer().getName());
        resp.setAssignedToName(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null);
        resp.setSlaDeadline(ticket.getSlaDeadline());
        resp.setCreatedAt(ticket.getCreatedAt());
        resp.setUpdatedAt(ticket.getUpdatedAt());
        return resp;
    }
}
