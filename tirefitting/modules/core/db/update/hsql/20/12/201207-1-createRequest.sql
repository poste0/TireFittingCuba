create table TIREFITTING_REQUEST (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REQUESTTYPE varchar(255),
    WHEELRADIUS integer,
    TIME_ date,
    servicePoint varchar(36),
    --
    primary key (ID)
);