create table if not exists game
(
    id                          varchar(100)       primary key ,
    status                      varchar(50)        not null,
    player_one_id               varchar(100)       not null,
    player_two_id               varchar(100),
    winner_id                   varchar(100),
    created_at                  timestamp          not null ,
    FOREIGN KEY (player_one_id) references player (id),
    FOREIGN KEY (player_two_id) references player (id),
    FOREIGN KEY (winner_id)     references player (id)
);

create index if not exists created_at_idx on game(created_at);
