-- ============================================================
--  ServiceFlow DB – Schema Creation Script
--  Run this ONCE before starting the application.
--  Engine: MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS serviceflow_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE serviceflow_db;

-- ── 1. USERS ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    email      VARCHAR(150)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    role       ENUM('ROLE_CUSTOMER','ROLE_SUPPORT','ROLE_ADMIN') NOT NULL,
    phone      VARCHAR(20),
    active     BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role  (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 2. TICKET CATEGORIES ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS ticket_categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active      BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 3. TICKETS ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS tickets (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(30)  NOT NULL UNIQUE,
    title         VARCHAR(200) NOT NULL,
    description   TEXT         NOT NULL,
    status        ENUM('OPEN','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN',
    priority      ENUM('LOW','MEDIUM','HIGH','CRITICAL')                    NOT NULL DEFAULT 'MEDIUM',
    customer_id   BIGINT       NOT NULL,
    assigned_to   BIGINT       NULL,
    category_id   BIGINT       NOT NULL,
    sla_deadline  TIMESTAMP    NOT NULL,
    resolved_at   TIMESTAMP    NULL,
    closed_at     TIMESTAMP    NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ticket_customer  FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_assignee  FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_ticket_category  FOREIGN KEY (category_id) REFERENCES ticket_categories(id),
    INDEX idx_ticket_number   (ticket_number),
    INDEX idx_ticket_status   (status),
    INDEX idx_ticket_customer (customer_id),
    INDEX idx_ticket_assigned (assigned_to),
    INDEX idx_ticket_sla      (sla_deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 4. TICKET COMMENTS ───────────────────────────────────────
CREATE TABLE IF NOT EXISTS ticket_comments (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id  BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    comment    TEXT   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id)   ON DELETE CASCADE,
    CONSTRAINT fk_comment_user   FOREIGN KEY (user_id)   REFERENCES users(id)     ON DELETE CASCADE,
    INDEX idx_comment_ticket (ticket_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 5. TICKET HISTORY ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ticket_history (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id    BIGINT       NOT NULL,
    changed_by   BIGINT       NOT NULL,
    old_status   VARCHAR(30),
    new_status   VARCHAR(30),
    old_priority VARCHAR(30),
    new_priority VARCHAR(30),
    remarks      VARCHAR(500),
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_ticket     FOREIGN KEY (ticket_id)  REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_history_changed_by FOREIGN KEY (changed_by) REFERENCES users(id),
    INDEX idx_history_ticket (ticket_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 6. NOTIFICATIONS ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notifications (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    title       VARCHAR(150)  NOT NULL,
    message     TEXT          NOT NULL,
    read_status BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notif_user   (user_id),
    INDEX idx_notif_unread (user_id, read_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 7. FEEDBACK ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS feedback (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id   BIGINT UNIQUE NOT NULL,
    customer_id BIGINT        NOT NULL,
    rating      INT           NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comments    TEXT,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_ticket   FOREIGN KEY (ticket_id)   REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_customer FOREIGN KEY (customer_id) REFERENCES users(id)   ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 8. ATTACHMENTS ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS attachments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id         BIGINT        NOT NULL,
    uploaded_by       BIGINT        NOT NULL,
    original_filename VARCHAR(255)  NOT NULL,
    stored_filename   VARCHAR(255)  NOT NULL,
    content_type      VARCHAR(100)  NOT NULL,
    file_size         BIGINT        NOT NULL,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attach_ticket FOREIGN KEY (ticket_id)   REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_attach_user   FOREIGN KEY (uploaded_by) REFERENCES users(id),
    INDEX idx_attach_ticket (ticket_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
