-- begin TIREFITTING_SERVICE_POINT
create table TIREFITTING_SERVICE_POINT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ADDRESS varchar(255),
    COUNTOFSTUFF integer,
    --
    primary key (ID)
)^
-- end TIREFITTING_SERVICE_POINT
-- begin TIREFITTING_REQUEST
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
    time timestamp,
    servicePoint varchar(36),
    --
    primary key (ID)
)^
-- end TIREFITTING_REQUEST
