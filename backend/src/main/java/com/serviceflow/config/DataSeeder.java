package com.serviceflow.config;

import com.serviceflow.entity.TicketCategory;
import com.serviceflow.entity.User;
import com.serviceflow.entity.enums.Role;
import com.serviceflow.repository.TicketCategoryRepository;
import com.serviceflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the database with initial data on startup.
 * Only active on the "dev" profile (H2 in-memory).
 * For production MySQL, use db/seed.sql directly.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TicketCategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedCategories();
        seedUsers();
        log.info("✅ ServiceFlow dev database seeded successfully.");
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(TicketCategory.builder().name("Technical Support")
                .description("Hardware, software and connectivity issues").active(true).build());
        categoryRepository.save(TicketCategory.builder().name("Billing & Payments")
                .description("Invoice disputes, payment failures, refunds").active(true).build());
        categoryRepository.save(TicketCategory.builder().name("Account Management")
                .description("Password resets, profile updates, access issues").active(true).build());
        categoryRepository.save(TicketCategory.builder().name("Product Feedback")
                .description("Feature requests and product improvement ideas").active(true).build());
        categoryRepository.save(TicketCategory.builder().name("General Inquiry")
                .description("Any other questions or information requests").active(true).build());

        log.info("✅ Seeded 5 ticket categories.");
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        String hash = passwordEncoder.encode("Password@123");

        userRepository.save(User.builder().name("Admin User")
                .email("admin@serviceflow.com").password(hash)
                .role(Role.ROLE_ADMIN).phone("+91-9000000001").active(true).build());

        userRepository.save(User.builder().name("Support Agent 1")
                .email("support1@serviceflow.com").password(hash)
                .role(Role.ROLE_SUPPORT).phone("+91-9000000002").active(true).build());

        userRepository.save(User.builder().name("Support Agent 2")
                .email("support2@serviceflow.com").password(hash)
                .role(Role.ROLE_SUPPORT).phone("+91-9000000003").active(true).build());

        userRepository.save(User.builder().name("Alice Customer")
                .email("alice@example.com").password(hash)
                .role(Role.ROLE_CUSTOMER).phone("+91-9111111111").active(true).build());

        userRepository.save(User.builder().name("Bob Customer")
                .email("bob@example.com").password(hash)
                .role(Role.ROLE_CUSTOMER).phone("+91-9222222222").active(true).build());

        log.info("✅ Seeded 5 users (password: Password@123).");
    }
}
