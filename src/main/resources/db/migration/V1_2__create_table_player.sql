create table if not exists player (
    id       varchar(100) primary key,
    username varchar(255) not null unique
);
