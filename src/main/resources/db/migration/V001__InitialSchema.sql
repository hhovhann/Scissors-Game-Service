-- Create USERS table
CREATE TABLE IF NOT EXISTS USER_ENTITY (
                                           USER_ID    VARCHAR(255) PRIMARY KEY,
    USERNAME   VARCHAR(255) NOT NULL UNIQUE,
    EMAIL      VARCHAR(255) NOT NULL UNIQUE,
    PASSWORD   VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create GAME table
CREATE TABLE IF NOT EXISTS GAME_ENTITY (
    ID            BIGSERIAL PRIMARY KEY,
    USER_ID       VARCHAR(255) NOT NULL,
    USER_MOVE     VARCHAR(255) NOT NULL,
    COMPUTER_MOVE VARCHAR(255) NOT NULL,
    TIMESTAMP     TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RESULT        VARCHAR(255) NOT NULL,
    STATUS        VARCHAR(255) NOT NULL CHECK (STATUS IN ('ACTIVE', 'TERMINATED')), -- Example of status values
    FOREIGN KEY (USER_ID) REFERENCES USER_ENTITY(USER_ID) ON DELETE CASCADE
);
