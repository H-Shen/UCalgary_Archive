show databases; /* show all current databases */
drop database if exists seng_513_project; /* drop the old database */
create database if not exists seng_513_project; /* create the database for our project */
use seng_513_project;

/* Create the table for 'user' */
create table if not exists user
(
    uuid                        varchar(36)  not null,
    username                    varchar(256) not null,
    email                       varchar(256) not null,
    password                    varchar(256) not null,
    password_question           varchar(256) not null,
    answer_of_password_question varchar(256) not null,
    primary key (uuid)
) default charset =utf8mb4;

/* Create the picture table for 'user' */
create table if not exists user_pic
(
    uuid varchar(36) not null,
    filename varchar(512) not null,
    user_uuid varchar(36) not null,
    primary key (uuid)
) default charset =utf8mb4;
alter table user_pic
	add constraint user_pic_user_uuid_fk
		foreign key (user_uuid) references user (uuid)
			on update cascade on delete cascade;

/* Create the table for 'team' */
create table if not exists team
(
    uuid varchar(36) not null,

    primary key (uuid)
) default charset =utf8mb4;

/* Create the relationship between table for 'team' */
create table if not exists team2members
(
    team_uuid varchar(36) not null,
    member_uuid varchar(36) not null
) default charset =utf8mb4;
alter table team2members
    add constraint team_uuid_team_uuid_fk
        foreign key (team_uuid) references team (uuid)
            on update cascade on delete cascade;
alter table team2members
    add constraint member_uuid_user_uuid_fk
        foreign key (member_uuid) references user (uuid)
            on update cascade on delete cascade;

/* Create the picture table for 'team' */
create table if not exists team_pic
(
    uuid varchar(36) not null,
    filename varchar(512) not null,
    team_uuid varchar(36) not null,
    primary key (uuid)
) default charset =utf8mb4;
alter table team_pic
    add constraint team_pic_team_uuid_fk
        foreign key (team_uuid) references team (uuid)
            on update cascade on delete cascade;


/* Create the table for 'comment' */
create table if not exists comment
(
    uuid varchar(36) not null,
    time datetime not null,
    sender_username varchar(36) not null,
    team_uuid varchar(36) not null,
    contents varchar(1024) not null,
    primary key (uuid)
);
alter table comment
    add constraint team_uuid__team_uuid_fk
        foreign key (team_uuid) references team (uuid)
            on update cascade on delete cascade;

/* Create the table for 'administrator' */
create table if not exists admin
(
    uuid     varchar(36)  not null,
    username varchar(256)  not null,
    password varchar(256) not null,
    primary key (uuid)
);

/* Insert some users */
insert ignore into user values (UUID(), 'user0', 'samuellee@test.com', 'password0', 'question0', 'answer0');
insert ignore into user values (UUID(), 'user1', 'jamesoconnell@test.com', 'password1', 'question1', 'answer1');
insert ignore into user values (UUID(), 'user2', 'nickpit12314@test.com', 'password2', 'question2', 'answer2');
insert ignore into user values (UUID(), 'SamuelL2', 'samuellee@test.com', 'password0', 'question0', 'answer0');
insert ignore into user values (UUID(), 'JamesOC2', 'jamesoconnell@test.com', 'password1', 'question0', 'answer1');
insert ignore into user values (UUID(), 'NickPit2', 'nickpit12314@test.com', 'password2', 'question0', 'answer2');






