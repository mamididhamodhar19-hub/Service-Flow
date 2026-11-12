package com.serviceflow.repository;

import com.serviceflow.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByTicketId(Long ticketId);

    boolean existsByTicketId(Long ticketId);

    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.ticket.assignedTo.id = :agentId")
    Double findAverageRatingByAgent(Long agentId);
}
