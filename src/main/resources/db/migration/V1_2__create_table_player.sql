create table if not exists player (
    id       varchar(100) primary key,
    username varchar(255) not null unique
);

create unique index if not exists username_idx on player(username);
