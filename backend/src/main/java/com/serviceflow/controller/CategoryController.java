package com.serviceflow.controller;

import com.serviceflow.entity.TicketCategory;
import com.serviceflow.repository.TicketCategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Ticket Categories", description = "Endpoints for managing ticket categories")
public class CategoryController {

    private final TicketCategoryRepository categoryRepository;

    @GetMapping
    @Operation(summary = "Get all ticket categories")
    public ResponseEntity<List<TicketCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
}
