-- create_chat_schema.sql

CREATE SCHEMA IF NOT EXISTS chat_service AUTHORIZATION core_api_user;

-- Table: user_token
CREATE TABLE IF NOT EXISTS chat_service.user_token (
    id SERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    UNIQUE (token)
);

-- Table: messages
CREATE TABLE IF NOT EXISTS chat_service.messages (
    id SERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    message_type VARCHAR(20) DEFAULT 'text', -- text, image, file, etc.
    channel_id TEXT -- optional if you want group or direct channels
);
