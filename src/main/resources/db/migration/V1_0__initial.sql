
create table appuser (
    id serial primary key,
    email varchar(60) not null unique,
    name varchar(60) not null default 'User'
);

create table campaign (
    id serial primary key,
    user_id integer not null REFERENCES appuser (id),
    name varchar(60) not null,
    active boolean not null default true
);

create table gallery (
    id serial primary key,
    campaign_id integer not null REFERENCES campaign (id),
    name varchar(60) not null,
    content text not null default '',
    cache text default '',
    active boolean not null default true
);