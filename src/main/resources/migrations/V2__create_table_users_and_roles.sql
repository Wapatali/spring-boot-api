create table roles(
    id uuid primary key default uuid_generate_v4(),
    authority varchar(128) not null unique
);

create table users(
    id uuid primary key default uuid_generate_v4(),
    username varchar(128) not null unique,
    password varchar(60) not null,
    account_non_expired boolean not null default true,
    account_non_locked boolean not null default true,
    credentials_non_expired boolean not null default true,
    enabled boolean not null default true
);

create table users_roles(
    user_id uuid references users(id),
    role_id uuid references roles(id),
    primary key (user_id, role_id)
);