CREATE TABLE IF NOT EXISTS daily_messages (
    daily_messages_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content           VARCHAR(200),
    assigned_at       DATETIME
);

CREATE TABLE IF NOT EXISTS users (
    user_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    daily_messages_id  BIGINT,
    kakao_id           VARCHAR(30)  NOT NULL,
    nickname           VARCHAR(10)  NOT NULL,
    email              VARCHAR(100),
    profile_icon       VARCHAR(30),
    subscription_tier  VARCHAR(10)  NOT NULL,
    created_at         DATETIME     NOT NULL,
    updated_at         DATETIME     NOT NULL,
    deleted_at         DATETIME,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_users_daily_message FOREIGN KEY (daily_messages_id) REFERENCES daily_messages (daily_messages_id)
);

CREATE TABLE IF NOT EXISTS worry (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT,
    content              VARCHAR(255) NOT NULL,
    scheduled_start_at   DATETIME     NOT NULL,
    scheduled_end_at     DATETIME     NOT NULL,
    emotion_score_before INT          NOT NULL,
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    deleted_at           DATETIME,
    is_deleted           BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_worry_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS worry_note (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    deleted_at DATETIME,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS mind_sessions (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT       NOT NULL,
    worry_id             BIGINT       NOT NULL UNIQUE,
    status               VARCHAR(10)  NOT NULL,
    worry_content        TEXT         NOT NULL,
    theme_category       VARCHAR(10),
    emotion_score_before INT,
    scheduled_start_at   DATETIME     NOT NULL,
    scheduled_end_at     DATETIME     NOT NULL,
    started_at           DATETIME,
    completed_at         DATETIME,
    emotion_score_after  INT,
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    deleted_at           DATETIME,
    is_deleted           BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS session_records (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id  BIGINT      NOT NULL,
    record_type VARCHAR(20) NOT NULL,
    content_text TEXT,
    media_url   VARCHAR(500),
    created_at  DATETIME    NOT NULL,
    updated_at  DATETIME    NOT NULL,
    deleted_at  DATETIME,
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS recipes (
    recipe_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT,
    title             VARCHAR(20)  NOT NULL,
    description       VARCHAR(200),
    estimated_minutes INT,
    created_at        DATETIME     NOT NULL,
    updated_at        DATETIME     NOT NULL,
    deleted_at        DATETIME,
    is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS recipe_logs (
    recipe_logs_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT   NOT NULL,
    recipe_id      BIGINT   NOT NULL,
    executed_at    DATETIME,
    is_completed   BOOLEAN,
    created_at     DATETIME NOT NULL,
    updated_at     DATETIME NOT NULL,
    deleted_at     DATETIME,
    is_deleted     BOOLEAN  NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    type            VARCHAR(30)  NOT NULL,
    title           VARCHAR(100) NOT NULL,
    body            VARCHAR(500),
    is_read         BOOLEAN      NOT NULL,
    read_at         DATETIME,
    reference_id    BIGINT,
    reference_type  VARCHAR(20),
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    deleted_at      DATETIME,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS notification_settings (
    id                      BIGINT  AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT  NOT NULL UNIQUE,
    worry_reminder_enabled  BOOLEAN NOT NULL,
    session_start_enabled   BOOLEAN NOT NULL,
    created_at              DATETIME NOT NULL,
    updated_at              DATETIME NOT NULL,
    deleted_at              DATETIME,
    is_deleted              BOOLEAN  NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS favorite_time_slots (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    label      VARCHAR(255) NOT NULL,
    start_time TIME         NOT NULL,
    end_time   TIME         NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    deleted_at DATETIME,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_favorite_time_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS example (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    deleted_at DATETIME,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);
