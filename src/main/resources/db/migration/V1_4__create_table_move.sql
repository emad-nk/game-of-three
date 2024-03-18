create table if not exists move
(
    id             varchar(100)    primary key,
    value          integer,
    current_result integer         not null,
    timestamp      timestamp       not null,
    player_id      varchar(100)    not null,
    game_id        varchar(100)    not null,
    foreign key (player_id)        references player (id),
    foreign key (game_id)          references game (id)
);
