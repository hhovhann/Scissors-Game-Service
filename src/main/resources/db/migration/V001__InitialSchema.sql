CREATE TABLE IF NOT EXISTS game
(
    ID            bigint       not null auto_increment,
    TIMESTAMP     datetime(6)  not null,
    COMPUTER_MOVE varchar(255) not null,
    RESULT        varchar(255) not null,
    STATUS        varchar(255) not null,
    USER_MOVE     varchar(255) not null,
    primary key (ID)
);