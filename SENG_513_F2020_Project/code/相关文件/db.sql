create database if not exists seng_513_project; /* create the database for our project */
show databases; /* show all current databases */
use seng_513_project;
/* choose the database we are gonna use */

/* Remove old all tables and procedures */
drop table if exists admin, members_of_groups, pic, portrait, user, workgroup, comments;
drop procedure if exists selfDeletionCheck;

/* Create the table for 'user' */
create table if not exists user
(
    uuid                        varchar(36)  not null,
    username                    varchar(36)  not null, /* the length of the username <= 36 */
    email                       varchar(256) not null, /* the length of the email <= 256 */ /* Validate email format on front-end */
    password                    varchar(256) not null, /* the length of the password <= 256 */
    password_question           varchar(256) not null, /* the length of the password_question <= 256 */
    answer_of_password_question varchar(256) not null, /* the length of the answer of password_question <= 256 */
    constraint user_username_uindex
        unique (username),
    primary key (uuid)
);
/* password question minimum length >= 6 */
-- alter table user
--     add constraint password_question_min_length
--         check ( length(password_question) >= 6 );
-- /* answer for the password question minimum length >= 6 */
-- alter table user
--     add constraint answer_for_password_question_min_length
--         check ( length(answer_of_password_question) >= 6 );
-- /* password minimum length >= 6 */
-- alter table user
--     add constraint password_min_length
--         check ( length(password) >= 6 );
-- /* username minimum length >= 6 */
-- alter table user
--     add constraint username_minimum_length
--         check ( length(username) >= 6 );

create table workgroup
(
    code int auto_increment,
    name varchar(36) not null,
    primary key (code),
    constraint workgroup_code_uindex
        unique (code)
);
/* group name minimum length >= 6 */
alter table workgroup
    add constraint name_minimum_length
        check ( length(name) >= 6 );

/* Create the table for 'image' */
create table if not exists pic
(
    uuid              varchar(36)  not null,
    relative_filename varchar(512) not null, /* the length of relative name <= 512, for safety, make the name of the picture <= 256 */
    user_uuid         varchar(36)  null, /* it is not null if it is a user's image */
    group_code    int          null,     /* it is not null if it is a group's image */
    primary key (uuid),
    constraint pic_relative_path_uindex
        unique (relative_filename) /* unique relative filename */
);

/* Create the table for user's 'portrait' */
create table if not exists portrait
(
    uuid              varchar(36)                             not null,
    relative_filename varchar(512) default 'default_portrait' not null,
    user_uuid         varchar(36)                             not null,
    primary key (uuid),
    constraint portrait_user_uuid_fk
        foreign key (user_uuid) references user (uuid)
            on update cascade on delete cascade
);

/* Create the relation between group and its members */
create table if not exists members_of_groups
(
    group_code  int         not null,
    member_uuid varchar(36) not null,
    constraint members_of_groups_user_uuid_fk
        foreign key (member_uuid) references user (uuid)
            on update cascade on delete cascade,
    constraint members_of_groups_workgroup_code_fk
        foreign key (group_code) references workgroup (code)
            on update cascade on delete cascade,
    unique key `unique_members_of_groups` (group_code, member_uuid)
);

/* define the procedure for self-deletion */
create procedure selfDeletionCheck(in groupCode int)
begin
    /* do self-deletion if no one is in the group */
    if (select count(*) from members_of_groups where members_of_groups.group_code = groupCode) = 0 then
        /* 1. delete all pics it has */
        delete from pic where pic.group_code = groupCode;
        /* 2. delete it in the members_of_groups */
        delete from members_of_groups where members_of_groups.group_code = groupCode;
    end if;
end;

/* Create the table for 'administrator' */
create table if not exists admin
(
    uuid     varchar(36)  not null,
    username varchar(36)  not null, /* length of username <= 36 */
    password varchar(256) not null, /* length of password <= 256 */
    constraint admin_username_uindex
        unique (username),
    primary key (uuid)
);
/* password minimum length >= 6 */
alter table admin
    add constraint admin_password_min_length
        check ( length(password) >= 6 );
/* username minimum length >= 6 */
alter table admin
    add constraint admin_username_minimum_length
        check ( length(username) >= 6 );

/** Format control will be left to front-end **/
/* username of user/admin must contain letters or digits or underscore only */
/* email validity check */
/* password of user/admin must in printable ASCII only except \t\n\r\x0b\x0c */

/* create 6 random users */
insert ignore into user values (UUID(), 'SamuelL', 'samuellee@test.com', 'password0', 'question0', 'answer0');
insert ignore into user values (UUID(), 'JamesOC', 'jamesoconnell@test.com', 'password1', 'question1', 'answer1');
insert ignore into user values (UUID(), 'NickPit', 'nickpit12314@test.com', 'password2', 'question2', 'answer2');
insert ignore into user values (UUID(), 'SamuelL2', 'samuellee@test.com', 'password0', 'question0', 'answer0');
insert ignore into user values (UUID(), 'JamesOC2', 'jamesoconnell@test.com', 'password1', 'question0', 'answer1');
insert ignore into user values (UUID(), 'NickPit2', 'nickpit12314@test.com', 'password2', 'question0', 'answer2');
