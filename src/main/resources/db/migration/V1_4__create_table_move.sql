create table if not exists move
(
    id        varchar(100)    PRIMARY KEY,
    value     INTEGER         NOT NULL,
    timestamp TIMESTAMP       NOT NULL,
    player_id varchar(100)    NOT NULL,
    game_id   varchar(100)    NOT NULL,
    FOREIGN KEY (player_id)   REFERENCES player (id),
    FOREIGN KEY (game_id)     REFERENCES game (id)
);
