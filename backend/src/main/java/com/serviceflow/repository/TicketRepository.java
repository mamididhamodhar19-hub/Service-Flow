package com.serviceflow.repository;

import com.serviceflow.entity.Ticket;
import com.serviceflow.entity.enums.Priority;
import com.serviceflow.entity.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    // ── Customer-scoped queries ──────────────────────────────
    List<Ticket> findByCustomerId(Long customerId);

    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);

    Page<Ticket> findByCustomerIdAndStatus(Long customerId, TicketStatus status, Pageable pageable);

    // ── Support agent queries ────────────────────────────────
    List<Ticket> findByAssignedToId(Long agentId);

    Page<Ticket> findByAssignedToId(Long agentId, Pageable pageable);

    Page<Ticket> findByAssignedToIdAndStatus(Long agentId, TicketStatus status, Pageable pageable);

    // ── Admin / general queries ──────────────────────────────
    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByPriority(Priority priority, Pageable pageable);

    Page<Ticket> findByStatusAndPriority(TicketStatus status, Priority priority, Pageable pageable);

    // ── SLA breach detection ─────────────────────────────────
    @Query("""
            SELECT t FROM Ticket t
            WHERE t.slaDeadline < :now
              AND t.status NOT IN ('RESOLVED', 'CLOSED')
            ORDER BY t.slaDeadline ASC
            """)
    List<Ticket> findSlaBreachedTickets(@Param("now") LocalDateTime now);

    // ── Dashboard analytics ──────────────────────────────────
    long countByStatus(TicketStatus status);

    long countByPriority(Priority priority);

    long countByAssignedToId(Long agentId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo.id = :agentId AND t.status NOT IN ('RESOLVED','CLOSED')")
    long countOpenTicketsByAgent(@Param("agentId") Long agentId);

    @Query("""
            SELECT COUNT(t) FROM Ticket t
            WHERE t.slaDeadline < :now
              AND t.status NOT IN ('RESOLVED','CLOSED')
            """)
    long countSlaBreached(@Param("now") LocalDateTime now);

    @Query("""
            SELECT t FROM Ticket t
            LEFT JOIN FETCH t.customer
            LEFT JOIN FETCH t.assignedTo
            LEFT JOIN FETCH t.category
            WHERE t.id = :id
            """)
    Optional<Ticket> findByIdWithDetails(@Param("id") Long id);

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:categoryId IS NULL OR t.category.id = :categoryId)
              AND (:assignedToId IS NULL OR t.assignedTo.id = :assignedToId)
            """)
    Page<Ticket> filterTickets(
            @Param("status")      TicketStatus status,
            @Param("priority")    Priority priority,
            @Param("categoryId")  Long categoryId,
            @Param("assignedToId") Long assignedToId,
            Pageable pageable
    );
}
