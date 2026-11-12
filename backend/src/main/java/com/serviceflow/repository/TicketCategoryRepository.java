package com.serviceflow.repository;

import com.serviceflow.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {

    List<TicketCategory> findByActiveTrue();

    Optional<TicketCategory> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
