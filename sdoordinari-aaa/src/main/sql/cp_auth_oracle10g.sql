drop table CP_APPLICATION_AUD cascade constraints;
drop table cp_application_permiss cascade constraints;
drop table cp_application_permiss_AUD cascade constraints;
drop table cp_application_resource cascade constraints;
drop table cp_application_resource_AUD cascade constraints;
drop table cp_application_role cascade constraints;
drop table cp_application_role_AUD cascade constraints;
drop table CP_CONTACT cascade constraints;
drop table CP_CONTACT_AUD cascade constraints;
drop table CP_LOCATION cascade constraints;
drop table CP_LOCATION_AUD cascade constraints;
drop table CP_OPERATION cascade constraints;
drop table CP_OPERATION_AUD cascade constraints;
drop table CP_ORGANIZATION cascade constraints;
drop table CP_ORGANIZATION_AUD cascade constraints;
drop table CP_PERMISSION cascade constraints;
drop table CP_PERMISSION_AUD cascade constraints;
drop table CP_PERMISSION_TYPE cascade constraints;
drop table CP_PERMISSION_TYPE_AUD cascade constraints;
drop table CP_RESOURCE cascade constraints;
drop table CP_RESOURCE_AUD cascade constraints;
drop table CP_RESOURCE_TYPE cascade constraints;
drop table CP_RESOURCE_TYPE_AUD cascade constraints;
drop table CP_ROLE cascade constraints;
drop table CP_ROLE_AUD cascade constraints;
drop table CP_ROLE_GROUP cascade constraints;
drop table CP_ROLE_GROUP_AUD cascade constraints;
drop table cp_role_group_role cascade constraints;
drop table cp_role_group_role_AUD cascade constraints;
drop table CP_SESSION cascade constraints;
drop table CP_SITE cascade constraints;
drop table CP_SITE_AUD cascade constraints;
drop table CP_SITE_DETAIL cascade constraints;
drop table CP_SITE_DETAIL_AUD cascade constraints;
drop table CP_SITE_DETAIL_TYPE cascade constraints;
drop table CP_SITE_DETAIL_TYPE_AUD cascade constraints;
drop table cp_site_role cascade constraints;
drop table cp_site_role_AUD cascade constraints;
drop table cp_site_user cascade constraints;
drop table cp_site_user_AUD cascade constraints;
drop table CP_USER cascade constraints;
drop table CP_USER_AUD cascade constraints;
drop table CP_USER_DETAIL cascade constraints;
drop table CP_USER_DETAIL_AUD cascade constraints;
drop table CP_USER_DETAIL_TYPE cascade constraints;
drop table CP_USER_DETAIL_TYPE_AUD cascade constraints;
drop table cp_user_organization cascade constraints;
drop table cp_user_organization_AUD cascade constraints;
drop table CP_USER_PICTURE cascade constraints;
drop table CP_USER_PICTURE_AUD cascade constraints;
drop table cp_user_role cascade constraints;
drop table cp_user_role_AUD cascade constraints;
drop table cp_user_role_group cascade constraints;
drop table cp_user_role_group_AUD cascade constraints;
drop table REVINFO cascade constraints;
 

create sequence hibernate_sequence start with 1 increment by 1;

    create table CP_APPLICATION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        ENABLED number(1,0),
        URL varchar2(255 char),
        primary key (id)
    );

    create table CP_APPLICATION_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        ENABLED number(1,0),
        URL varchar2(255 char),
        primary key (id, REV)
    );

    create table cp_application_permiss (
        APPLICATION_ID varchar2(255 char) not null,
        PERMISSION_ID varchar2(255 char) not null,
        primary key (APPLICATION_ID, PERMISSION_ID)
    );

    create table cp_application_permiss_AUD (
        REV number(10,0) not null,
        APPLICATION_ID varchar2(255 char) not null,
        PERMISSION_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, APPLICATION_ID, PERMISSION_ID)
    );

    create table cp_application_resource (
        APPLICATION_ID varchar2(255 char) not null,
        RESOURCE_ID varchar2(255 char) not null,
        primary key (APPLICATION_ID, RESOURCE_ID)
    );

    create table cp_application_resource_AUD (
        REV number(10,0) not null,
        APPLICATION_ID varchar2(255 char) not null,
        RESOURCE_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, APPLICATION_ID, RESOURCE_ID)
    );

    create table cp_application_role (
        APPLICATION_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        primary key (APPLICATION_ID, ROLE_ID)
    );

    create table cp_application_role_AUD (
        REV number(10,0) not null,
        APPLICATION_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, APPLICATION_ID, ROLE_ID)
    );

    create table CP_CONTACT (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        EMAIL varchar2(255 char),
        INTERNATIONAL_AREA_CODE varchar2(255 char),
        NAME varchar2(255 char),
        PHONE_NUMBER varchar2(255 char),
        PHONE_PREFIX varchar2(255 char),
        SURNAME varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_CONTACT_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        EMAIL varchar2(255 char),
        INTERNATIONAL_AREA_CODE varchar2(255 char),
        NAME varchar2(255 char),
        PHONE_NUMBER varchar2(255 char),
        PHONE_PREFIX varchar2(255 char),
        SURNAME varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_LOCATION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        ADDRESS varchar2(255 char),
        CITY varchar2(255 char),
        COUNTRY varchar2(255 char),
        HOUSE_NUMBER varchar2(255 char),
        LATITUDE float,
        LONGITUDE float,
        ZIP_CODE varchar2(255 char),
        USER_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_LOCATION_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        ADDRESS varchar2(255 char),
        CITY varchar2(255 char),
        COUNTRY varchar2(255 char),
        HOUSE_NUMBER varchar2(255 char),
        LATITUDE float,
        LONGITUDE float,
        ZIP_CODE varchar2(255 char),
        USER_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_OPERATION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        OPERATION_TYPE varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_OPERATION_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        OPERATION_TYPE varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_ORGANIZATION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        CODE varchar2(255 char),
        NAME varchar2(255 char),
        CONTACT_ID varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_ORGANIZATION_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        CODE varchar2(255 char),
        NAME varchar2(255 char),
        CONTACT_ID varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_PERMISSION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        endDate timestamp,
        startDate timestamp,
        DESCRIPTION varchar2(255 char),
        APPLICATION_ID varchar2(255 char),
        ORGANIZATION_ID varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        ROLE_ID varchar2(255 char),
        SITE_ID varchar2(255 char),
        PERMISSION_TYPE_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_PERMISSION_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        APPLICATION_ID varchar2(255 char),
        ORGANIZATION_ID varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        ROLE_ID varchar2(255 char),
        SITE_ID varchar2(255 char),
        PERMISSION_TYPE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_PERMISSION_TYPE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        primary key (id)
    );

    create table CP_PERMISSION_TYPE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_RESOURCE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        RESOURCE_ID varchar2(255 char),
        RESOURCE_QNAME varchar2(255 char),
        RESOURCE_TYPE_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_RESOURCE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        RESOURCE_ID varchar2(255 char),
        RESOURCE_QNAME varchar2(255 char),
        RESOURCE_TYPE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_RESOURCE_TYPE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        primary key (id)
    );

    create table CP_RESOURCE_TYPE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_ROLE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        endDate timestamp,
        startDate timestamp,
        DESCRIPTION varchar2(255 char),
        SESSION_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_ROLE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_ROLE_GROUP (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        primary key (id)
    );

    create table CP_ROLE_GROUP_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        primary key (id, REV)
    );

    create table cp_role_group_role (
        ROLE_GROUP_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        primary key (ROLE_GROUP_ID, ROLE_ID)
    );

    create table cp_role_group_role_AUD (
        REV number(10,0) not null,
        ROLE_GROUP_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, ROLE_GROUP_ID, ROLE_ID)
    );

    create table CP_SESSION (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        SESSION_DETAILS varchar2(255 char),
        SESSION_END timestamp,
        SESSION_IP_ADDRESS varchar2(255 char),
        SESSION_RENEWED timestamp,
        SESSION_START timestamp,
        ORGANIZATION_ID varchar2(255 char),
        ROLE_ID varchar2(255 char),
        SITE_ID varchar2(255 char),
        USER_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_SITE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        CONTACT_ID varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        ORGANIZATION_ID varchar2(255 char),
        FATHER_SITE_ID varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        SESSION_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_SITE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        CONTACT_ID varchar2(255 char),
        LOCATION_ID varchar2(255 char),
        ORGANIZATION_ID varchar2(255 char),
        FATHER_SITE_ID varchar2(255 char),
        RESOURCE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_SITE_DETAIL (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DETAIL varchar2(255 char),
        SITE_ID varchar2(255 char),
        SITE_DETAIL_TYPE_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_SITE_DETAIL_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DETAIL varchar2(255 char),
        SITE_ID varchar2(255 char),
        SITE_DETAIL_TYPE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_SITE_DETAIL_TYPE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DESCRIPTION varchar2(255 char),
        primary key (id)
    );

    create table CP_SITE_DETAIL_TYPE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DESCRIPTION varchar2(255 char),
        primary key (id, REV)
    );

    create table cp_site_role (
        SITE_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        primary key (SITE_ID, ROLE_ID)
    );

    create table cp_site_role_AUD (
        REV number(10,0) not null,
        SITE_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, SITE_ID, ROLE_ID)
    );

    create table cp_site_user (
        SITE_ID varchar2(255 char) not null,
        USER_ID varchar2(255 char) not null,
        primary key (SITE_ID, USER_ID)
    );

    create table cp_site_user_AUD (
        REV number(10,0) not null,
        SITE_ID varchar2(255 char) not null,
        USER_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, SITE_ID, USER_ID)
    );

    create table CP_USER (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        ENABLED number(1,0),
        NAME varchar2(255 char),
        PASSWORD varchar2(255 char),
        PASSWORD_CHANGE_TIMESTAMP timestamp,
        SURNAME varchar2(255 char),
        USERNAME varchar2(255 char),
        USER_PICTURE_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_USER_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        ENABLED number(1,0),
        NAME varchar2(255 char),
        PASSWORD varchar2(255 char),
        PASSWORD_CHANGE_TIMESTAMP timestamp,
        SURNAME varchar2(255 char),
        USERNAME varchar2(255 char),
        USER_PICTURE_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_USER_DETAIL (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        DETAIL varchar2(255 char),
        USER_DETAIL_TYPE_ID varchar2(255 char),
        USER_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_USER_DETAIL_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        DETAIL varchar2(255 char),
        USER_DETAIL_TYPE_ID varchar2(255 char),
        USER_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table CP_USER_DETAIL_TYPE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        CLAIM_URI varchar2(255 char),
        TYPE varchar2(255 char),
        primary key (id)
    );

    create table CP_USER_DETAIL_TYPE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        CLAIM_URI varchar2(255 char),
        TYPE varchar2(255 char),
        primary key (id, REV)
    );

    create table cp_user_organization (
        USER_ID varchar2(255 char) not null,
        ORGANIZATION_ID varchar2(255 char) not null,
        primary key (USER_ID, ORGANIZATION_ID)
    );

    create table cp_user_organization_AUD (
        REV number(10,0) not null,
        USER_ID varchar2(255 char) not null,
        ORGANIZATION_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, USER_ID, ORGANIZATION_ID)
    );

    create table CP_USER_PICTURE (
        id varchar2(255 char) not null,
        createdBy varchar2(255 char),
        createdDate timestamp,
        lastModifiedBy varchar2(255 char),
        lastModifiedDate timestamp,
        IMAGE_B64 varchar2(255 char),
        IMG_HEIGHT number(10,0),
        IMG_WIDTH number(10,0),
        USER_ID varchar2(255 char),
        primary key (id)
    );

    create table CP_USER_PICTURE_AUD (
        id varchar2(255 char) not null,
        REV number(10,0) not null,
        REVTYPE number(3,0),
        IMAGE_B64 varchar2(255 char),
        IMG_HEIGHT number(10,0),
        IMG_WIDTH number(10,0),
        USER_ID varchar2(255 char),
        primary key (id, REV)
    );

    create table cp_user_role (
        USER_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        primary key (USER_ID, ROLE_ID)
    );

    create table cp_user_role_AUD (
        REV number(10,0) not null,
        USER_ID varchar2(255 char) not null,
        ROLE_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, USER_ID, ROLE_ID)
    );

    create table cp_user_role_group (
        USER_ID varchar2(255 char) not null,
        ROLE_GROUP_ID varchar2(255 char) not null,
        primary key (USER_ID, ROLE_GROUP_ID)
    );

    create table cp_user_role_group_AUD (
        REV number(10,0) not null,
        USER_ID varchar2(255 char) not null,
        ROLE_GROUP_ID varchar2(255 char) not null,
        REVTYPE number(3,0),
        primary key (REV, USER_ID, ROLE_GROUP_ID)
    );

    create table REVINFO (
        REV number(10,0) not null,
        REVTSTMP number(19,0),
        primary key (REV)
    );
create index CP_CONTACT_IDX on CP_CONTACT (INTERNATIONAL_AREA_CODE);
create index CP_LOCATION_IDX on CP_LOCATION (HOUSE_NUMBER);
create index CP_LOCATION_2_IDX on CP_LOCATION (ZIP_CODE);
create index CP_LOCATION_3_IDX on CP_LOCATION (USER_ID);
create index CP_ORGANIZATION_IDX on CP_ORGANIZATION (CODE);
create index CP_ORGANIZATION_2_IDX on CP_ORGANIZATION (CONTACT_ID);
create index CP_ORGANIZATION_3_IDX on CP_ORGANIZATION (LOCATION_ID);
create index CP_PERMISSION_IDX on CP_PERMISSION (PERMISSION_TYPE_ID);
create index CP_PERMISSION_2_IDX on CP_PERMISSION (ROLE_ID);
create index CP_PERMISSION_3_IDX on CP_PERMISSION (RESOURCE_ID);
create index CP_PERMISSION_4_IDX on CP_PERMISSION (APPLICATION_ID);
create index CP_PERMISSION_5_IDX on CP_PERMISSION (SITE_ID);
create index CP_PERMISSION_6_IDX on CP_PERMISSION (ORGANIZATION_ID);

    alter table CP_PERMISSION_TYPE 
        add constraint UK_stvk1sbp4bfkwtn7yeohm2e3x unique (DESCRIPTION);
create index CP_RESOURCE_IDX on CP_RESOURCE (RESOURCE_TYPE_ID);

    alter table CP_RESOURCE_TYPE 
        add constraint UK_r0wa1n67wawxypf6rxqc4ka07 unique (DESCRIPTION);
create index CP_SESSION_IDX on CP_SESSION (USER_ID);
create index CP_SESSION_2_IDX on CP_SESSION (ORGANIZATION_ID);
create index CP_SITE_IDX on CP_SITE (RESOURCE_ID);
create index CP_SITE_2_IDX on CP_SITE (LOCATION_ID);
create index CP_SITE_3_IDX on CP_SITE (CONTACT_ID);
create index CP_SITE_DETAIL_IDX on CP_SITE_DETAIL (SITE_ID);
create index CP_SITE_DETAIL_2_IDX on CP_SITE_DETAIL (SITE_DETAIL_TYPE_ID);

    alter table CP_SITE_DETAIL_TYPE 
        add constraint UK_mhsyfw8p321eehub434iai1lg unique (DESCRIPTION);
create index CP_USER_DETAIL_IDX on CP_USER_DETAIL (USER_ID);
create index CP_USER_DETAIL_2_IDX on CP_USER_DETAIL (USER_DETAIL_TYPE_ID);

    alter table CP_USER_DETAIL_TYPE 
        add constraint UK_qa0a4hv6owoqkeja3hnkk850h unique (TYPE);

    alter table CP_APPLICATION_AUD 
        add constraint FKpfdf284yjsgctf37y6uxrhgpa 
        foreign key (REV) 
        references REVINFO;

    alter table cp_application_permiss 
        add constraint FKsbf4uc3rvn4cjbm3uqowg1pup 
        foreign key (PERMISSION_ID) 
        references CP_PERMISSION;

    alter table cp_application_permiss 
        add constraint FK6m7861bdjsgan6n7m91isur3v 
        foreign key (APPLICATION_ID) 
        references CP_APPLICATION;

    alter table cp_application_permiss_AUD 
        add constraint FKy7xquda7ko8o7twa9nfut2t0 
        foreign key (REV) 
        references REVINFO;

    alter table cp_application_resource 
        add constraint FK6ncnb488a1ixdj8povfm78gr6 
        foreign key (RESOURCE_ID) 
        references CP_RESOURCE;

    alter table cp_application_resource 
        add constraint FK9mpr65jy9mfxyc3ckipd5uybx 
        foreign key (APPLICATION_ID) 
        references CP_APPLICATION;

    alter table cp_application_resource_AUD 
        add constraint FK3oiq5chjik49etohu083a6bit 
        foreign key (REV) 
        references REVINFO;

    alter table cp_application_role 
        add constraint FKk6vk8bi39wldkddrw45phv2iv 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table cp_application_role 
        add constraint FK70sv0b31ysbnm1y5sg164k380 
        foreign key (APPLICATION_ID) 
        references CP_APPLICATION;

    alter table cp_application_role_AUD 
        add constraint FK97h4gs3p57069wx0byqei3vqc 
        foreign key (REV) 
        references REVINFO;

    alter table CP_CONTACT 
        add constraint FKgiphwaujy95xcbjywthtok3j1 
        foreign key (LOCATION_ID) 
        references CP_LOCATION;

    alter table CP_CONTACT_AUD 
        add constraint FKtgcuakl6fc5wynai8d9cxwja5 
        foreign key (REV) 
        references REVINFO;

    alter table CP_LOCATION 
        add constraint FKa055s2b4ijn9wh295e0nh91hi 
        foreign key (USER_ID) 
        references CP_USER;

    alter table CP_LOCATION_AUD 
        add constraint FKrp15np9hv6dlx3ceh1to5t6ih 
        foreign key (REV) 
        references REVINFO;

    alter table CP_OPERATION 
        add constraint FKchvid98tgbybd82xq33idccql 
        foreign key (RESOURCE_ID) 
        references CP_RESOURCE;

    alter table CP_OPERATION_AUD 
        add constraint FK1627g6m5sjgwxmr4c292nomct 
        foreign key (REV) 
        references REVINFO;

    alter table CP_ORGANIZATION 
        add constraint FKmvymfv9p2eadxdq1a5god53dt 
        foreign key (CONTACT_ID) 
        references CP_CONTACT;

    alter table CP_ORGANIZATION 
        add constraint FKe82w2dr1w7ie36l9k9m3i02hg 
        foreign key (LOCATION_ID) 
        references CP_LOCATION;

    alter table CP_ORGANIZATION_AUD 
        add constraint FKfoqocyne1fof18326ckivcw6v 
        foreign key (REV) 
        references REVINFO;

    alter table CP_PERMISSION 
        add constraint FKam2vyi02baq9at94d6b6osuh5 
        foreign key (APPLICATION_ID) 
        references CP_APPLICATION;

    alter table CP_PERMISSION 
        add constraint FKte3fm5f0706pfemo0e9vjmvgn 
        foreign key (ORGANIZATION_ID) 
        references CP_ORGANIZATION;

    alter table CP_PERMISSION 
        add constraint FK4uwpqjy3cgh9rl37s4i1xbgsm 
        foreign key (RESOURCE_ID) 
        references CP_RESOURCE;

    alter table CP_PERMISSION 
        add constraint FKoawdnjj9qk6rc1qch4frxo77i 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table CP_PERMISSION 
        add constraint FKcakk40um0mm6xho8v3r2vhrr 
        foreign key (SITE_ID) 
        references CP_SITE;

    alter table CP_PERMISSION 
        add constraint FK44igvkyq4jl874g8xyvx6nyvb 
        foreign key (PERMISSION_TYPE_ID) 
        references CP_PERMISSION_TYPE;

    alter table CP_PERMISSION_AUD 
        add constraint FKsmf8o4sdipa8699af13m91b3l 
        foreign key (REV) 
        references REVINFO;

    alter table CP_PERMISSION_TYPE_AUD 
        add constraint FKlklnbcoeg3plgbf23t5bmjo77 
        foreign key (REV) 
        references REVINFO;

    alter table CP_RESOURCE 
        add constraint FKk4n57l3hnqmhfcx0btyibyipi 
        foreign key (RESOURCE_TYPE_ID) 
        references CP_RESOURCE_TYPE;

    alter table CP_RESOURCE_AUD 
        add constraint FK1h9mvae16damn523aijvnutlv 
        foreign key (REV) 
        references REVINFO;

    alter table CP_RESOURCE_TYPE_AUD 
        add constraint FKlctlt96s1ipmp0ul1g3q8raro 
        foreign key (REV) 
        references REVINFO;

    alter table CP_ROLE 
        add constraint FKlu35383my6yc8667m0eq68b1m 
        foreign key (SESSION_ID) 
        references CP_SESSION;

    alter table CP_ROLE_AUD 
        add constraint FK91gh0osboubkjyx63ok6wf9g6 
        foreign key (REV) 
        references REVINFO;

    alter table CP_ROLE_GROUP_AUD 
        add constraint FK75x7fl19c7f74uh18oh08bpxe 
        foreign key (REV) 
        references REVINFO;

    alter table cp_role_group_role 
        add constraint FK6mulanvbaptdn7c9qqm43k8dt 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table cp_role_group_role 
        add constraint FKb94iiiaybni15he3hvykovcvy 
        foreign key (ROLE_GROUP_ID) 
        references CP_ROLE_GROUP;

    alter table cp_role_group_role_AUD 
        add constraint FK3xw6i4kht57yrvkhn1ga0tbwf 
        foreign key (REV) 
        references REVINFO;

    alter table CP_SESSION 
        add constraint FKbr2gs705d4pqmc2nw353ubyhh 
        foreign key (ORGANIZATION_ID) 
        references CP_ORGANIZATION;

    alter table CP_SESSION 
        add constraint FKlsv8n507odv5umvbpgq7hdv0n 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table CP_SESSION 
        add constraint FK6w1fpxqf158sbhq21dqmholo0 
        foreign key (SITE_ID) 
        references CP_SITE;

    alter table CP_SESSION 
        add constraint FK14ubhk80ocn2oo1g1ciy5upw6 
        foreign key (USER_ID) 
        references CP_USER;

    alter table CP_SITE 
        add constraint FKpw5kujfm8qbtgxbwgxyxgoelq 
        foreign key (CONTACT_ID) 
        references CP_CONTACT;

    alter table CP_SITE 
        add constraint FKkv7qebhhg6u4vusefvv631n23 
        foreign key (LOCATION_ID) 
        references CP_LOCATION;

    alter table CP_SITE 
        add constraint FKs3offc8s9nc54quym8w6f90fb 
        foreign key (ORGANIZATION_ID) 
        references CP_ORGANIZATION;

    alter table CP_SITE 
        add constraint FKgkw0j9wmsijpy3k300n8dj6hg 
        foreign key (FATHER_SITE_ID) 
        references CP_SITE;

    alter table CP_SITE 
        add constraint FK52cx8gg0ahumgcsqn1mq5y2mf 
        foreign key (RESOURCE_ID) 
        references CP_RESOURCE;

    alter table CP_SITE 
        add constraint FKguvj0r7njq4dlqb0jj4tive3l 
        foreign key (SESSION_ID) 
        references CP_SESSION;

    alter table CP_SITE_AUD 
        add constraint FKg42pq0k1n9tkdvfy95rtriiyh 
        foreign key (REV) 
        references REVINFO;

    alter table CP_SITE_DETAIL 
        add constraint FKghba9h3qep9b1440idib0apqq 
        foreign key (SITE_ID) 
        references CP_SITE;

    alter table CP_SITE_DETAIL 
        add constraint FK66otn5an4m2yjk7nf4961wt0y 
        foreign key (SITE_DETAIL_TYPE_ID) 
        references CP_SITE_DETAIL_TYPE;

    alter table CP_SITE_DETAIL_AUD 
        add constraint FK3obo6x1u0kbp78gskwq61ihrx 
        foreign key (REV) 
        references REVINFO;

    alter table CP_SITE_DETAIL_TYPE_AUD 
        add constraint FK5sfwowvvy7ot3oq12oixo2qyu 
        foreign key (REV) 
        references REVINFO;

    alter table cp_site_role 
        add constraint FK71u134x2aa1lrq2t48jsfbgfm 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table cp_site_role 
        add constraint FKmyfiawpjsv1fs0lmpfdkpo8bu 
        foreign key (SITE_ID) 
        references CP_SITE;

    alter table cp_site_role_AUD 
        add constraint FKdpfv1poadtusvlm4ld1m3y7yc 
        foreign key (REV) 
        references REVINFO;

    alter table cp_site_user 
        add constraint FKq3l7n3w5gfmxcnfu1plyf2tvc 
        foreign key (USER_ID) 
        references CP_USER;

    alter table cp_site_user 
        add constraint FKoqgme78y0wgnhto4nop3kpswh 
        foreign key (SITE_ID) 
        references CP_SITE;

    alter table cp_site_user_AUD 
        add constraint FK1btkir8qhpp8tc6e6hlldedqe 
        foreign key (REV) 
        references REVINFO;

    alter table CP_USER 
        add constraint FK58fkjsp37yej40kpnw6b3gmqx 
        foreign key (USER_PICTURE_ID) 
        references CP_USER_PICTURE;

    alter table CP_USER_AUD 
        add constraint FKox69ab4kf5w11t3msbayth3qm 
        foreign key (REV) 
        references REVINFO;

    alter table CP_USER_DETAIL 
        add constraint FKau3l61jd70ucto7efusanbakh 
        foreign key (USER_DETAIL_TYPE_ID) 
        references CP_USER_DETAIL_TYPE;

    alter table CP_USER_DETAIL 
        add constraint FK25vke8o3a0ipcj6lymf6qftje 
        foreign key (USER_ID) 
        references CP_USER;

    alter table CP_USER_DETAIL_AUD 
        add constraint FK8cihjqvgtml44h333voxhskjb 
        foreign key (REV) 
        references REVINFO;

    alter table CP_USER_DETAIL_TYPE_AUD 
        add constraint FK2uxof9ei18nmspj926j8lqhji 
        foreign key (REV) 
        references REVINFO;

    alter table cp_user_organization 
        add constraint FKe3gd4pbtpoi94ecdrgh48gk3p 
        foreign key (ORGANIZATION_ID) 
        references CP_ORGANIZATION;

    alter table cp_user_organization 
        add constraint FK4iaxyeql0kp0bc9gqf95tuit2 
        foreign key (USER_ID) 
        references CP_USER;

    alter table cp_user_organization_AUD 
        add constraint FKrk03qji14c7gbwa074wee64i8 
        foreign key (REV) 
        references REVINFO;

    alter table CP_USER_PICTURE 
        add constraint FKsj4vr0tkklb52c1gp095knyaw 
        foreign key (USER_ID) 
        references CP_USER;

    alter table CP_USER_PICTURE_AUD 
        add constraint FKpemv2qmh4roym29m1mdfwbi47 
        foreign key (REV) 
        references REVINFO;

    alter table cp_user_role 
        add constraint FKlxg85jhvvp8qu8hmsasxkqffv 
        foreign key (ROLE_ID) 
        references CP_ROLE;

    alter table cp_user_role 
        add constraint FKbs1ay3w7evp96riuxfsas7ahy 
        foreign key (USER_ID) 
        references CP_USER;

    alter table cp_user_role_AUD 
        add constraint FKctwno4wfp7qs3l81mxv1hjbv8 
        foreign key (REV) 
        references REVINFO;

    alter table cp_user_role_group 
        add constraint FK2wrb8j1ctvv8ymkiue4jdx0fn 
        foreign key (ROLE_GROUP_ID) 
        references CP_ROLE_GROUP;

    alter table cp_user_role_group 
        add constraint FK86heuseahmmmkh6rf6p8g48fo 
        foreign key (USER_ID) 
        references CP_USER;

    alter table cp_user_role_group_AUD 
        add constraint FK1noshng358drk6hcrstsyyae7 
        foreign key (REV) 
        references REVINFO;
