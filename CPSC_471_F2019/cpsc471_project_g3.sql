/*
  1. the sql statements in this file and the pseudo-code list in the hipo functions includes all
     possible statements we can think of at this moment.
  2. we use mysql ver 8.0.18 as our sql dialect.
  3. we use uuid as primary keys(id) for client, item, administrator, comment, order and we use
     varchar(36) to store them internally as strings.
     for better readability for clients and administrators.
  4. we change the keyword in idea_list as a json array that contains non-empty unique strings(use triggers/php to validate),
     and remove the relation idea_list_keyword for simplify the logic.
  5. some triggers are not finished completely and will be completed later with the help of php, such as the json array in
     a tuple of idea_list should contain unique, non-empty string objects and its length should have an upper-bound.
 */

drop database if exists cpsc_471_project_g3; /* delete the old database if exists */
create database cpsc_471_project_g3; /* create the database for our project */
show databases; /* show all current databases */
use cpsc_471_project_g3;
/* choose the database we are gonna use */

/**** create all tables, check constraints, events, triggers without foreign keys ****/
/* create table item */
create table `item`
(
    `id`                  varchar(36)   not null,
    `name`                varchar(30)   not null,
    `page_visit_counter`  int(11)       not null default '0',
    `description`         varchar(100)  not null,
    `condition`           varchar(30)   not null default 'used_good',
    `price`               decimal(8, 2) not null default '0.00',
    `type`                varchar(30)   not null,
    `days_to_expire`      int(11)       not null default '365',
    `picture`             varchar(256)           default null comment 'a picture is a file path to its image',
    `administrator_id`    varchar(36)            default null,
    `client_id_of_buyer`  varchar(36)            default null,
    `client_id_of_seller` varchar(36)   not null,
    primary key (`id`)
);
/* deletes expired items base on a daily check */
delimiter $$
create event if not exists expired_items_check
    on schedule every 1 day starts date_add(curdate(), interval 1 day)
    on completion preserve enable
    do delete
       from item
       where days_to_expire = 0;
delimiter ;
/* set the minimum length of the item's description */
alter table item
    add constraint description_min_length
        check ( length(description) >= 5 );
/* set the minimum length of the item's name */
alter table item
    add constraint name_min_length
        check ( length(name) >= 3 );
/* validate the item's condition */
alter table item
    add constraint item_condition
        check ( `condition` in (
                                'used_acceptable', /* the item is fairly worn but continues to function properly. */
                                'used_good', /* the item shows wear from consistent use, but it remains in good condition and functions properly. */
                                'used_very_good', /* the item has seen limited use and remains in good working condition */
                                'used_open_box', /* the item in perfect working condition. and the user just unwraps it just a while ago. */
                                'new') );
/* the item is completely without being unwrapped. */
/* validate the item's price */
alter table item
    add constraint item_price
        check ( price >= 0.00 );
/* validate the item's type */
alter table item
    add constraint item_type
        check ( type in
                ('books', /*books, calendars, card decks, sheet music, issues of magazines and journals, and other publications*/
                 'electronic_books', /*e-books, course materials ... */
                 'consumer_electronics', /*tv, cd player , camera, cellphone, charger ... */
                 'food', /*snack, beverages */
                 'personal_computers', /*desktops, laptops, drives, storage, raspberrypi ...*/
                 'software', /*windows 10 disc image, autocad, ansys ...*/
                 'sports_and_outdoors', /*sports equipment, athletic shoes, bicycles ... */
                 'music', /*cds, recordings, cassettes ... */
                 'musical_instrument', /*guitars, keyboards ... */
                 'video_games', /*game consoles, ps4 dualshock 4 ... */
                 'clothes', /*used clothes, cosplay outfits ... */
                 'office_products', /*gel-pens, geometry compass set combination compass ...*/
                 'others') /*others*/
            );

/* create table comment */
create table `comment`
(
    `id`        varchar(36)  not null,
    `item_id`   varchar(36)  not null,
    `client_id` varchar(36)  not null,
    `post_date` date         not null,
    `content`   varchar(512) not null,
    primary key (`id`)
);
/* set the minimum length of the comment */
alter table comment
    add constraint comment_min_length
        check ( length(content) >= 5 );

/* comment post_date trigger */
/* a new inserted comment item automatically has current date as its post_date */
create trigger comment_post_date_insert
    before insert
    on comment
    for each row
begin
    set new.post_date = curdate();
end;

/* create table client */
create table `client`
(
    `id`                          varchar(36)  not null,
    `ucid`                        int(11)      not null,
    `password`                    varchar(256) not null,
    `password_question`           varchar(256) not null,
    `answer_of_password_question` varchar(256) not null,
    `address`                     varchar(256) not null,
    `account_status`              varchar(10)  not null default 'offline',
    `phone_number`                varchar(20)           default null,
    `date_of_registration`        date         not null,
    `username`                    varchar(20)  not null,
    unique key `client_username_uindex` (`username`),
    primary key (`id`)
);

/* password question minimum length */
alter table client
    add constraint password_question_min_length
        check ( length(password_question) >= 6 );

/* answer for the password question minimum length */
alter table client
    add constraint answer_for_password_question_min_length
        check ( length(answer_of_password_question) >= 6 );

/* password minimum length */
alter table client
    add constraint password_min_length
        check ( length(password) >= 6 );
/* username minimum length */
alter table client
    add constraint username_minimum_length
        check ( length(username) >= 6 );
/* validate the account status */
alter table client
    add constraint validate_account_status
        check ( account_status in ('active', 'offline') );
/* validate the phone number */
create trigger phone_number_format_check_before_insert
    before insert
    on client
    for each row
begin
    if (new.phone_number regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;
create trigger phone_number_format_check_before_update
    before update
    on client
    for each row
begin
    if (new.phone_number regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;
/* client date_of_registration trigger */
/* a new inserted client item automatically has current date as its date_of_registration */
create trigger client_date_of_registration_insert
    before insert
    on client
    for each row
begin
    set new.date_of_registration = curdate();
end;

/* create table seller */
create table `seller`
(
    `client_id` varchar(36) not null,
    primary key (`client_id`)
);

/* create table buyer */
create table `buyer`
(
    `client_id` varchar(36) not null,
    primary key (`client_id`)
);

/* create table ucalgary_member */
create table `ucalgary_member`
(
    `ucid`                 int(11)        not null auto_increment,
    `campus_email_address` varchar(256)   not null,
    `date_of_birth`        date           not null,
    `gender`               char(1)        not null,
    `balance`              decimal(10, 2) not null default '0.00',
    `first_name`           varchar(30)    not null,
    `middle_initial`       char(1)                 default null,
    `last_name`            varchar(30)    not null,
    primary key (`ucid`)
);

/* campus email address validator **/
create trigger campus_email_address_format_check_before_insert
    before insert
    on ucalgary_member
    for each row
begin
    if (new.campus_email_address regexp
        '^[a-za-z0-9][a-za-z0-9._-]*[a-za-z0-9._-]@[a-za-z0-9][a-za-z0-9._-]*[a-za-z0-9]\\.[a-za-z]{2,63}$') =
       0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid email format!';
    end if;
end;

create trigger campus_email_address_format_check_before_update
    before update
    on ucalgary_member
    for each row
begin
    if (new.campus_email_address regexp
        '^[a-za-z0-9][a-za-z0-9._-]*[a-za-z0-9._-]@[a-za-z0-9][a-za-z0-9._-]*[a-za-z0-9]\\.[a-za-z]{2,63}$') =
       0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid email format!';
    end if;
end;
/* gender validator */
alter table ucalgary_member
    add constraint gender_check
        check ( gender = 'm' or gender = 'f' );
/* balance validator */
alter table ucalgary_member
    add constraint balance_check
        check ( balance >= 0.00 );

/* create table announcement */
create table `announcement`
(
    `id`        varchar(36)  not null,
    `content`   varchar(512) not null,
    `post_date` date         not null,
    `title`     varchar(30)  not null,
    primary key (`id`)
);
/* announcement content minimum length */
alter table announcement
    add constraint content_minimum_length
        check ( length(content) >= 6 );

/* announcement post_date trigger */
/* a new inserted announcement item automatically has current date as its post_date */
create trigger announcement_post_date_insert
    before insert
    on announcement
    for each row
begin
    set new.post_date = curdate();
end;

/* create table administrator */
create table `administrator`
(
    `id`                   varchar(36) not null,
    `password`             varchar(50) not null,
    `address`              varchar(50)          default null,
    `phone_number`         varchar(20)          default null,
    `date_of_registration` date        not null default '2019-01-01',
    `date_of_birth`        date        not null default '1900-01-01',
    `username`             varchar(20) not null,
    `first_name`           varchar(30) not null,
    `middle_initial`       char(1)              default null,
    `last_name`            varchar(30) not null,
    primary key (`id`),
    unique key `administrator_username_uindex` (`username`)
);
/* password minimum length */
alter table administrator
    add constraint administrator_password_min_length
        check ( length(password) >= 6 );

/* phone number validator */
create trigger administrator_phone_number_format_check_before_insert
    before insert
    on administrator
    for each row
begin
    if (new.phone_number regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;
create trigger administrator_phone_number_format_check_before_update
    before update
    on administrator
    for each row
begin
    if (new.phone_number regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;
/* administrator's username minimum length */
alter table administrator
    add constraint administrator_username_minimum_length
        check ( length(username) >= 6 );

/* create table order */
create table `order`
(
    `id`                         varchar(36)   not null,
    `item_id`                    varchar(36)   not null,
    `total_price`                decimal(8, 2) not null default '0.00',
    `address_of_receiver`        varchar(50)   not null,
    `shipping_method`            varchar(20)   not null default 'contact by buyer',
    `first_name_of_receiver`     varchar(30)   not null,
    `middle_initial_of_receiver` char(1)                default null,
    `last_name_of_receiver`      varchar(30)   not null,
    `phone_number_of_receiver`   varchar(20)   not null,
    `date_of_order`              date          not null,
    `client_id_of_seller`        varchar(36)   not null,
    `client_id_of_buyer`         varchar(36)   not null,
    primary key (`id`)
);
/* order total price validator */
alter table `order`
    add constraint total_price_validator
        check ( total_price >= 0.00 );
/* order shipping method validator */
alter table `order`
    add constraint shipping_method_validator
        check ( shipping_method in ('contact by buyer', 'online delivery'));
/* phone number of receiver validator */
create trigger receiver_phone_number_format_check_before_insert
    before insert
    on `order`
    for each row
begin
    if (new.phone_number_of_receiver regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;
create trigger receiver_phone_number_format_check_before_update
    before update
    on `order`
    for each row
begin
    if (new.phone_number_of_receiver regexp '^1\-[0-9]{3}\-[0-9]{3}\-[0-9]{3}$') = 0 then /* 1-123-123-123 */
        signal sqlstate '12345'
            set message_text = 'invalid phone format!';
    end if;
end;

/* order date_of_order trigger */
/* a new inserted order item automatically has current date as its date_of_order */
create trigger order_date_of_order_insert
    before insert
    on `order`
    for each row
begin
    set new.date_of_order = curdate();
end;

/* create table idea_list */
create table `idea_list`
(
    `client_id`   varchar(36)       not null,
    `list_number` int(11) default 1 not null, /* we need a trigger to make sure if one item is removed, then the list number will be re-sorted **/
    `name`        varchar(20)       not null,
    `keyword`     json              not null,
    `description` varchar(256)      not null,
    primary key (`client_id`, `list_number`),
    unique key `idea_list_list_number_uindex` (`list_number`)
);

/* initialize new idea_list obj with an empty keyword */
create trigger idea_list_keyword_initialize
    before insert
    on idea_list
    for each row
begin
    set new.keyword = json_array();
end;

/* add foreign keys for item */
alter table item
    add constraint administrator_id_fk
        foreign key (administrator_id) references administrator (id)
            on update cascade on delete cascade;
alter table item
    add constraint client_id_of_buyer_fk
        foreign key (client_id_of_buyer) references buyer (client_id)
            on update cascade on delete cascade;
alter table item
    add constraint client_id_of_seller_fk
        foreign key (client_id_of_seller) references seller (client_id)
            on update cascade on delete cascade;

/* add foreign keys for comment */
alter table comment
    add constraint client_id_fk
        foreign key (client_id) references client (id)
            on update cascade on delete cascade;
alter table comment
    add constraint item_id_fk
        foreign key (item_id) references item (id)
            on update cascade on delete cascade;

/* add foreign keys for seller */
alter table seller
    add constraint seller_fk
        foreign key (client_id) references client (id)
            on update cascade on delete cascade;

/* add foreign keys for buyer */
alter table buyer
    add constraint buyer_fk
        foreign key (client_id) references client (id)
            on update cascade on delete cascade;

/* add foreign keys for order */
alter table `order`
    add constraint order_client_id_of_buyer_fk
        foreign key (client_id_of_buyer) references buyer (client_id)
            on update cascade on delete cascade;
alter table `order`
    add constraint order_client_id_of_seller_fk
        foreign key (client_id_of_seller) references seller (client_id)
            on update cascade on delete cascade;
alter table `order`
    add constraint order_item_id_fk
        foreign key (item_id) references item (id)
            on update cascade on delete cascade;

/* add foreign keys for idea_list */
alter table idea_list
    add constraint idea_list_fk
        foreign key (client_id) references client (id)
            on update cascade on delete cascade;

/* add foreign keys for client */
alter table client
    add constraint client_id_ucid_fk
        foreign key (ucid) references ucalgary_member (ucid)
            on update cascade on delete cascade;

/* make sure middle initial in administrator is null or an uppercase english letter */
create trigger administrator_middle_init_insert
    before insert
    on administrator
    for each row
begin
    if (new.middle_initial is not null) and (new.middle_initial regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;
create trigger administrator_middle_init_update
    before update
    on administrator
    for each row
begin
    if (new.middle_initial is not null) and (new.middle_initial regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;
/* make sure middle initial in order is null or an uppercase english letter */
create trigger order_middle_init_insert
    before insert
    on `order`
    for each row
begin
    if (new.middle_initial_of_receiver is not null) and
       (new.middle_initial_of_receiver regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;
create trigger order_middle_init_update
    before update
    on `order`
    for each row
begin
    if (new.middle_initial_of_receiver is not null) and
       (new.middle_initial_of_receiver regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;

/* make sure middle initial in ucalgary_member is null or an uppercase english letter */
create trigger ucalgary_member_middle_init_insert
    before insert
    on ucalgary_member
    for each row
begin
    if (new.middle_initial is not null) and (new.middle_initial regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;
create trigger ucalgary_member_middle_init_update
    before update
    on ucalgary_member
    for each row
begin
    if (new.middle_initial is not null) and (new.middle_initial regexp binary '^[a-z]$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid middle initial!';
    end if;
end;

/* make sure first name and last name of administrator is in uppercase */
create trigger administrator_uppercase_name_insert
    before insert
    on administrator
    for each row
begin
    if (new.first_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;
create trigger administrator_uppercase_name_update
    before update
    on administrator
    for each row
begin
    if (new.first_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;
/* make sure first name and last name of order is in uppercase */
create trigger order_uppercase_name_insert
    before insert
    on `order`
    for each row
begin
    if (new.first_name_of_receiver regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name_of_receiver regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;
create trigger order_uppercase_name_update
    before update
    on `order`
    for each row
begin
    if (new.first_name_of_receiver regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name_of_receiver regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;
/* make sure first name and last name of ucalgary_member is in uppercase */
create trigger ucalgary_member_uppercase_name_insert
    before insert
    on ucalgary_member
    for each row
begin
    if (new.first_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;
create trigger ucalgary_member_uppercase_name_update
    before update
    on ucalgary_member
    for each row
begin
    if (new.first_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid first name!';
    elseif (new.last_name regexp binary '^[a-z]+$') != 0 then
        signal sqlstate '12345'
            set message_text = 'invalid last name!';
    end if;
end;

/* validate ucalgary_member date_of_birth */
alter table ucalgary_member
    add constraint ucalgary_member_bdate_validator
        check ( date_of_birth >= '1900-01-01');

/* validate administrator date_of_birth */
alter table administrator
    add constraint administrator_bdate_validator
        check ( date_of_birth >= '1900-01-01');


/* Populate some initial values */
/* insert 100 members into ucalgary_member*/
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100000, 'harrison_shen@uofc.ca', '1997-08-21', 'f', '0.00', 'HARRISON', 'S', 'SHEN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100001, 'sheila_atkinson@uofc.ca', '1989-03-14', 'f', '799.00', 'SHEILA', 'Q', 'ATKINSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100002, 'jacob_merritt@uofc.ca', '1999-03-13', 'm', '96.55', 'JACOB', 'Y', 'MERRITT');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100003, 'heather_palmer@uofc.ca', '1991-11-03', 'f', '0.00', 'HEATHER', null, 'PALMER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100004, 'brianna_marshall@uofc.ca', '1986-10-03', 'f', '710.00', 'BRIANNA', null, 'MARSHALL');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100005, 'tiffany_hines@uofc.ca', '2000-02-18', 'f', '0.00', 'TIFFANY', null, 'HINES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100006, 'kayla_bradford@uofc.ca', '1998-07-01', 'f', '828.77', 'KAYLA', null, 'BRADFORD');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100007, 'jennifer_thompson@uofc.ca', '1996-02-23', 'f', '307.00', 'JENNIFER', null, 'THOMPSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100008, 'albert_jones@uofc.ca', '1986-03-08', 'm', '0.00', 'ALBERT', null, 'JONES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100009, 'michael_burgess@uofc.ca', '1990-08-05', 'm', '583.77', 'MICHAEL', null, 'BURGESS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100010, 'david_santos@uofc.ca', '1986-11-09', 'm', '0.00', 'DAVID', 'W', 'SANTOS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100011, 'elizabeth_barrett@uofc.ca', '1993-07-24', 'f', '368.88', 'ELIZABETH', null, 'BARRETT');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100012, 'elizabeth_carroll@uofc.ca', '1989-10-07', 'f', '306.77', 'ELIZABETH', null, 'CARROLL');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100013, 'michael_blackwell@uofc.ca', '1997-12-11', 'm', '71.77', 'MICHAEL', null, 'BLACKWELL');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100014, 'patrick_obrien@uofc.ca', '1986-01-13', 'm', '209.00', 'PATRICK', 'M', 'OBRIEN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100015, 'roy_wilson@uofc.ca', '1986-12-14', 'm', '0.00', 'ROY', null, 'WILSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100016, 'jennifer_henderson@uofc.ca', '1985-07-10', 'f', '990.11', 'JENNIFER', 'Q', 'HENDERSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100017, 'tamara_wright@uofc.ca', '1993-10-23', 'f', '626.00', 'TAMARA', null, 'WRIGHT');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100018, 'meghan_carter@uofc.ca', '1999-11-26', 'f', '927.00', 'MEGHAN', null, 'CARTER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100019, 'david_howell@uofc.ca', '2000-07-12', 'm', '0.00', 'DAVID', 'N', 'HOWELL');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100020, 'ricky_grimes@uofc.ca', '1992-02-21', 'm', '0.00', 'RICKY', null, 'GRIMES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100021, 'brian_harrison@uofc.ca', '1996-11-17', 'm', '423.55', 'BRIAN', null, 'HARRISON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100022, 'kenneth_marks@uofc.ca', '1988-01-25', 'm', '0.00', 'KENNETH', 'T', 'MARKS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100023, 'jennifer_heath@uofc.ca', '1985-02-09', 'f', '0.00', 'JENNIFER', 'Z', 'HEATH');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100024, 'erik_martinez@uofc.ca', '1987-06-14', 'm', '0.00', 'ERIK', null, 'MARTINEZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100025, 'james_franco@uofc.ca', '2000-11-28', 'm', '413.00', 'JAMES', null, 'FRANCO');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100026, 'ann_hinton@uofc.ca', '1995-10-25', 'f', '304.88', 'ANN', 'F', 'HINTON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100027, 'steven_matthews@uofc.ca', '1997-02-11', 'm', '40.77', 'STEVEN', null, 'MATTHEWS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100028, 'nicole_fowler@uofc.ca', '1987-05-31', 'f', '627.00', 'NICOLE', 'V', 'FOWLER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100029, 'michael_boyd@uofc.ca', '1987-09-26', 'm', '0.00', 'MICHAEL', 'U', 'BOYD');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100030, 'kathy_smith@uofc.ca', '1996-05-27', 'f', '205.77', 'KATHY', null, 'SMITH');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100031, 'alexis_wise@uofc.ca', '1989-01-14', 'f', '114.33', 'ALEXIS', null, 'WISE');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100032, 'deanna_macias@uofc.ca', '1994-12-13', 'f', '198.00', 'DEANNA', 'Q', 'MACIAS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100033, 'dustin_gonzalez@uofc.ca', '1992-08-30', 'm', '927.44', 'DUSTIN', null, 'GONZALEZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100034, 'holly_buchanan@uofc.ca', '1995-10-12', 'f', '160.77', 'HOLLY', 'X', 'BUCHANAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100035, 'joshua_smith@uofc.ca', '1995-07-16', 'm', '605.00', 'JOSHUA', 'R', 'SMITH');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100036, 'raven_raymond@uofc.ca', '1987-01-15', 'f', '225.00', 'RAVEN', null, 'RAYMOND');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100037, 'beth_parker@uofc.ca', '1991-06-28', 'f', '678.11', 'BETH', null, 'PARKER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100038, 'tristan_ray@uofc.ca', '1988-06-01', 'm', '370.00', 'TRISTAN', null, 'RAY');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100039, 'daniel_martin@uofc.ca', '1997-01-03', 'm', '639.00', 'DANIEL', null, 'MARTIN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100040, 'vanessa_buchanan@uofc.ca', '1990-04-02', 'f', '0.00', 'VANESSA', 'U', 'BUCHANAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100041, 'nicholas_love@uofc.ca', '1985-05-01', 'm', '0.00', 'NICHOLAS', 'H', 'LOVE');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100042, 'jeanne_wright@uofc.ca', '1999-09-11', 'f', '0.00', 'JEANNE', 'X', 'WRIGHT');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100043, 'amanda_allen@uofc.ca', '1988-06-01', 'f', '456.55', 'AMANDA', null, 'ALLEN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100044, 'daniel_miller@uofc.ca', '1993-10-12', 'm', '0.00', 'DANIEL', null, 'MILLER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100045, 'john_jones@uofc.ca', '1986-07-16', 'm', '801.33', 'JOHN', null, 'JONES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100046, 'james_garza@uofc.ca', '1986-07-24', 'm', '725.99', 'JAMES', 'C', 'GARZA');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100047, 'jeremy_ramirez@uofc.ca', '1993-08-19', 'm', '790.11', 'JEREMY', 'R', 'RAMIREZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100048, 'ellen_erickson@uofc.ca', '1998-07-21', 'f', '168.00', 'ELLEN', null, 'ERICKSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100049, 'daniel_clark@uofc.ca', '1985-02-10', 'm', '0.00', 'DANIEL', null, 'CLARK');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100050, 'joseph_stein@uofc.ca', '1998-08-12', 'm', '312.00', 'JOSEPH', 'L', 'STEIN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100051, 'kimberly_thompson@uofc.ca', '1990-08-09', 'f', '933.00', 'KIMBERLY', null, 'THOMPSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100052, 'kenneth_payne@uofc.ca', '1991-12-25', 'm', '0.00', 'KENNETH', null, 'PAYNE');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100053, 'kelly_bradshaw@uofc.ca', '1987-07-19', 'f', '0.00', 'KELLY', null, 'BRADSHAW');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100054, 'wendy_anthony@uofc.ca', '1999-10-16', 'f', '665.11', 'WENDY', 'X', 'ANTHONY');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100055, 'richard_mayo@uofc.ca', '1995-09-06', 'm', '230.33', 'RICHARD', null, 'MAYO');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100056, 'jill_tucker@uofc.ca', '1988-09-02', 'f', '0.00', 'JILL', null, 'TUCKER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100057, 'katelyn_taylor@uofc.ca', '1990-06-21', 'f', '605.00', 'KATELYN', 'C', 'TAYLOR');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100058, 'keith_chavez@uofc.ca', '2000-02-27', 'm', '782.00', 'KEITH', null, 'CHAVEZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100059, 'amanda_newton@uofc.ca', '1988-04-02', 'f', '115.00', 'AMANDA', 'H', 'NEWTON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100060, 'courtney_james@uofc.ca', '1985-05-09', 'f', '418.00', 'COURTNEY', null, 'JAMES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100061, 'michael_olson@uofc.ca', '1993-06-08', 'm', '0.00', 'MICHAEL', null, 'OLSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100062, 'emily_mills@uofc.ca', '1986-08-29', 'f', '622.22', 'EMILY', null, 'MILLS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100063, 'elizabeth_barnes@uofc.ca', '1996-02-13', 'f', '858.00', 'ELIZABETH', 'G', 'BARNES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100064, 'cory_lopez@uofc.ca', '2000-10-05', 'm', '973.77', 'CORY', null, 'LOPEZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100065, 'paul_garcia@uofc.ca', '1986-08-25', 'm', '0.00', 'PAUL', 'Z', 'GARCIA');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100066, 'christopher_brennan@uofc.ca', '1986-07-07', 'm', '631.00', 'CHRISTOPHER', 'K', 'BRENNAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100067, 'bradley_trujillo@uofc.ca', '1986-12-23', 'm', '0.00', 'BRADLEY', 'A', 'TRUJILLO');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100068, 'steven_dominguez@uofc.ca', '1988-06-16', 'm', '0.00', 'STEVEN', 'X', 'DOMINGUEZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100069, 'lucas_keller@uofc.ca', '1995-07-21', 'm', '0.00', 'LUCAS', 'Y', 'KELLER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100070, 'albert_guzman@uofc.ca', '1989-11-17', 'm', '367.00', 'ALBERT', null, 'GUZMAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100071, 'jeremy_richardson@uofc.ca', '1987-12-10', 'm', '829.33', 'JEREMY', 'H', 'RICHARDSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100072, 'stephanie_richards@uofc.ca', '1990-09-29', 'f', '508.44', 'STEPHANIE', 'T', 'RICHARDS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100073, 'heather_crawford@uofc.ca', '1997-05-15', 'f', '0.00', 'HEATHER', 'Q', 'CRAWFORD');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100074, 'cynthia_estrada@uofc.ca', '1998-11-22', 'f', '537.00', 'CYNTHIA', null, 'ESTRADA');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100075, 'mario_knight@uofc.ca', '1987-12-27', 'm', '241.00', 'MARIO', 'P', 'KNIGHT');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100076, 'anthony_english@uofc.ca', '1988-07-26', 'm', '719.00', 'ANTHONY', 'I', 'ENGLISH');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100077, 'jennifer_nolan@uofc.ca', '1986-02-18', 'f', '443.00', 'JENNIFER', 'X', 'NOLAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100078, 'james_watkins@uofc.ca', '1999-06-06', 'm', '0.00', 'JAMES', 'M', 'WATKINS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100079, 'james_jones@uofc.ca', '1991-01-16', 'm', '91.77', 'JAMES', 'O', 'JONES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100080, 'sherry_thomas@uofc.ca', '1985-04-27', 'f', '0.00', 'SHERRY', 'N', 'THOMAS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100081, 'wanda_soto@uofc.ca', '1994-12-15', 'f', '0.00', 'WANDA', null, 'SOTO');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100082, 'christine_oliver@uofc.ca', '1988-01-17', 'f', '538.00', 'CHRISTINE', 'E', 'OLIVER');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100083, 'lauren_dean@uofc.ca', '1996-09-17', 'f', '0.00', 'LAUREN', null, 'DEAN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100084, 'rachel_mason@uofc.ca', '1998-11-23', 'f', '0.00', 'RACHEL', null, 'MASON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100085, 'samantha_aguilar@uofc.ca', '1995-03-06', 'f', '748.00', 'SAMANTHA', null, 'AGUILAR');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100086, 'kimberly_cruz@uofc.ca', '1997-07-05', 'f', '508.77', 'KIMBERLY', null, 'CRUZ');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100087, 'alison_young@uofc.ca', '1998-08-07', 'f', '224.00', 'ALISON', null, 'YOUNG');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100088, 'michael_robertson@uofc.ca', '1993-02-10', 'm', '421.00', 'MICHAEL', 'Q', 'ROBERTSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100089, 'raymond_bolton@uofc.ca', '1985-01-31', 'm', '0.00', 'RAYMOND', null, 'BOLTON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100090, 'samuel_burton@uofc.ca', '1986-09-25', 'm', '744.99', 'SAMUEL', null, 'BURTON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100091, 'jerry_contreras@uofc.ca', '1992-09-26', 'm', '39.99', 'JERRY', null, 'CONTRERAS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100092, 'patricia_jones@uofc.ca', '2000-04-02', 'f', '0.00', 'PATRICIA', null, 'JONES');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100093, 'april_brock@uofc.ca', '1986-05-26', 'f', '257.00', 'APRIL', 'M', 'BROCK');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100094, 'adam_maldonado@uofc.ca', '1995-12-16', 'm', '0.00', 'ADAM', 'G', 'MALDONADO');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100095, 'kristin_horne@uofc.ca', '1997-06-21', 'f', '995.55', 'KRISTIN', 'Y', 'HORNE');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100096, 'brian_blevins@uofc.ca', '1985-04-15', 'm', '851.00', 'BRIAN', 'S', 'BLEVINS');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100097, 'jennifer_henson@uofc.ca', '1985-05-01', 'f', '0.00', 'JENNIFER', 'S', 'HENSON');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100098, 'lisa_nguyen@uofc.ca', '1989-08-22', 'f', '722.22', 'LISA', 'I', 'NGUYEN');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100099, 'kelly_moore@uofc.ca', '1996-07-31', 'f', '606.00', 'KELLY', null, 'MOORE');
insert into ucalgary_member (ucid, campus_email_address, date_of_birth, gender, balance, first_name, middle_initial,
                             last_name)
values (100100, 'virginia_alvarado@uofc.ca', '1999-04-05', 'f', '0.00', 'VIRGINIA', null, 'ALVARADO');

/* insert 50 clients whose ucid from 100000-100050 */
delete
from client;
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100000, 'username', 'password_question_100000', 'answer_of_password_question_100000', '604 Graham Via Suite 037
Lake Amanda, ME 76360', '1-874-002-827', curdate(), 'username_100000');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100001, 'lAfX&IkDEn', 'password_question_100001', 'answer_of_password_question_100001', '392 Frank Inlet
Lake Scott, DC 78151', '1-179-990-224', curdate(), 'username_100001');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100002, 'peZ0QHUdb7', 'password_question_100002', 'answer_of_password_question_100002', '67941 Megan Ford Suite 431
Port Sheri, IL 88879', '1-272-894-798', curdate(), 'username_100002');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100003, 'GeWWARlfo#', 'password_question_100003', 'answer_of_password_question_100003', 'USNV Powell
FPO AE 35002', '1-195-503-592', curdate(), 'username_100003');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100004, 'i9%yFagv1J', 'password_question_100004', 'answer_of_password_question_100004', '469 Lamb Motorway Suite 346
Port Kristin, ND 83971', '1-523-933-387', curdate(), 'username_100004');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100005, '93CaQaQ46t', 'password_question_100005', 'answer_of_password_question_100005', 'USNV James
FPO AP 82665', '1-132-801-205', curdate(), 'username_100005');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100006, 'vMsL1oafXG', 'password_question_100006', 'answer_of_password_question_100006', '49956 Kevin Hollow
Port Timothy, DE 57408', '1-282-939-573', curdate(), 'username_100006');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100007, 'OFIj$K$91K', 'password_question_100007', 'answer_of_password_question_100007', '20023 Stein Shores
East Matthew, MA 42367', '1-133-332-021', curdate(), 'username_100007');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100008, 'Un8ALs3lbD', 'password_question_100008', 'answer_of_password_question_100008', '35495 Harvey Port
Meyerborough, TN 72500', '1-041-608-526', curdate(), 'username_100008');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100009, '8qaKBGgMqV', 'password_question_100009', 'answer_of_password_question_100009', '5378 William Forest Suite 161
East Andrewburgh, PA 93561', '1-224-351-290', curdate(), 'username_100009');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100010, '&Qj!6boH8s', 'password_question_100010', 'answer_of_password_question_100010', '981 Cobb Port Suite 839
Timothyfort, WV 09388', '1-374-977-544', curdate(), 'username_100010');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100011, 'EWRbAoPMLs', 'password_question_100011', 'answer_of_password_question_100011', '554 Terrance Creek Apt. 651
Sheilahaven, ME 47132', '1-152-264-364', curdate(), 'username_100011');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100012, 'Mh2dRJUTsr', 'password_question_100012', 'answer_of_password_question_100012', '413 Andrew Run
Port Jackburgh, TN 83724', '1-984-106-359', curdate(), 'username_100012');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100013, 'B#Iwo4iVUq', 'password_question_100013', 'answer_of_password_question_100013', '089 Davis Fields Suite 042
Bryanland, SD 75617', '1-144-836-204', curdate(), 'username_100013');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100014, 'e4h&gs7yyh', 'password_question_100014', 'answer_of_password_question_100014', '478 Alexander Squares Apt. 096
Port Caseystad, NC 18221', '1-697-648-018', curdate(), 'username_100014');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100015, 'l8n70hOHUo', 'password_question_100015', 'answer_of_password_question_100015', '257 Sherman Valley Suite 294
Heidiville, IN 37979', '1-710-557-415', curdate(), 'username_100015');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100016, 'G24M7$yaJN', 'password_question_100016', 'answer_of_password_question_100016', '741 Kendra Orchard
Jenniferchester, WA 00844', '1-059-102-635', curdate(), 'username_100016');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100017, 'nKlVyPPX7K', 'password_question_100017', 'answer_of_password_question_100017', '9509 Eric Roads
East Jack, NH 31559', '1-131-994-059', curdate(), 'username_100017');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100018, '96Ejww7&Kh', 'password_question_100018', 'answer_of_password_question_100018', '529 Richards Knolls
Tanyachester, NV 07409', '1-446-756-778', curdate(), 'username_100018');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100019, 'wLBW1fl!Zw', 'password_question_100019', 'answer_of_password_question_100019', '40754 Ali Mountains Apt. 043
West Albertchester, OH 58140', '1-194-641-221', curdate(), 'username_100019');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100020, '%zA84fxhj4', 'password_question_100020', 'answer_of_password_question_100020', '0279 Russell Village
South Justin, VT 19786', '1-603-748-803', curdate(), 'username_100020');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100021, 'xeA&1MQjyw', 'password_question_100021', 'answer_of_password_question_100021', '081 Kelly Mount
Haysfort, SC 20789', '1-820-729-414', curdate(), 'username_100021');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100022, 'qOdJiF!Ze0', 'password_question_100022', 'answer_of_password_question_100022', '468 Moore Underpass
Samanthaside, PA 51470', '1-271-146-240', curdate(), 'username_100022');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100023, 'P4yZzL8CRa', 'password_question_100023', 'answer_of_password_question_100023', '07559 Roberts Turnpike Suite 225
New Christinaland, NE 77438', '1-417-600-022', curdate(), 'username_100023');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100024, 'v&qbMRrI&A', 'password_question_100024', 'answer_of_password_question_100024', '5805 Dustin Dale
New Gracestad, LA 88505', '1-100-537-174', curdate(), 'username_100024');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100025, 'wtJ#cJiuES', 'password_question_100025', 'answer_of_password_question_100025', '608 Mann Islands
Kyleshire, LA 51232', '1-286-190-320', curdate(), 'username_100025');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100026, 'yOC4vkZmSE', 'password_question_100026', 'answer_of_password_question_100026', 'USCGC Howard
FPO AE 95775', '1-626-759-258', curdate(), 'username_100026');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100027, 'VLsSRBZKpV', 'password_question_100027', 'answer_of_password_question_100027', '28116 Leonard Fall Apt. 727
Woodsborough, NM 11673', '1-821-102-059', curdate(), 'username_100027');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100028, 'lbtNEyw7Ax', 'password_question_100028', 'answer_of_password_question_100028', '72692 Vargas Light Apt. 024
Samanthaside, OH 47548', '1-717-582-718', curdate(), 'username_100028');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100029, 'YOHkXm2LmW', 'password_question_100029', 'answer_of_password_question_100029', '680 Vance Avenue Suite 009
East Amy, VA 78787', '1-843-601-743', curdate(), 'username_100029');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100030, 'gr2cpxwAbS', 'password_question_100030', 'answer_of_password_question_100030', 'PSC 0074, Box 2036
APO AA 91215', '1-430-682-405', curdate(), 'username_100030');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100031, 'mQhFD8%IYE', 'password_question_100031', 'answer_of_password_question_100031', '8907 Anderson Overpass
Lake Steven, SD 18366', '1-797-637-861', curdate(), 'username_100031');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100032, 'iXmLFC!8Wr', 'password_question_100032', 'answer_of_password_question_100032', '389 Michael Road
Chaseside, IA 20407', '1-532-545-150', curdate(), 'username_100032');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100033, '0f805VHv9D', 'password_question_100033', 'answer_of_password_question_100033', '613 Gay Mountain Suite 278
Port Scottburgh, AK 04923', '1-734-192-965', curdate(), 'username_100033');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100034, 'jV2Y4LONpL', 'password_question_100034', 'answer_of_password_question_100034', '425 Burgess Hollow Apt. 511
Christineshire, NH 08580', '1-866-863-056', curdate(), 'username_100034');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100035, 'laS#AUiu9d', 'password_question_100035', 'answer_of_password_question_100035', '8679 Floyd Fields Suite 891
Morganstad, CO 51667', '1-061-186-035', curdate(), 'username_100035');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100036, 'cfbeTnz1qy', 'password_question_100036', 'answer_of_password_question_100036', '2020 Alex Way Suite 522
Catherineshire, VA 91685', '1-587-419-496', curdate(), 'username_100036');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100037, 'VIi!$gr44x', 'password_question_100037', 'answer_of_password_question_100037', 'Unit 4613 Box 9585
DPO AE 37232', '1-191-614-488', curdate(), 'username_100037');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100038, 'pLIAG#sDgr', 'password_question_100038', 'answer_of_password_question_100038', '60156 Butler Estates
West James, MS 51746', '1-294-214-906', curdate(), 'username_100038');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100039, 'zpFhJjKr!w', 'password_question_100039', 'answer_of_password_question_100039', '507 Ryan Garden
South Nicholeton, AL 26876', '1-209-059-811', curdate(), 'username_100039');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100040, 'p5IOYX4Kku', 'password_question_100040', 'answer_of_password_question_100040', 'PSC 2628, Box 9697
APO AA 80708', '1-764-883-999', curdate(), 'username_100040');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100041, '8YfXpjKjxY', 'password_question_100041', 'answer_of_password_question_100041', '5714 Bryan Path Suite 629
Seanfurt, WA 69302', '1-077-016-323', curdate(), 'username_100041');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100042, 'NkudOp&RMG', 'password_question_100042', 'answer_of_password_question_100042', '157 Bill Road
South Tylerburgh, PA 27938', '1-999-795-522', curdate(), 'username_100042');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100043, '$NYIovwbMd', 'password_question_100043', 'answer_of_password_question_100043', '72465 Tucker Cliffs
Raybury, AR 34335', '1-835-058-131', curdate(), 'username_100043');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100044, 'OuCw5eF10#', 'password_question_100044', 'answer_of_password_question_100044', '743 Patel Port
Parksport, WY 46314', '1-841-839-670', curdate(), 'username_100044');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100045, 'q8bnfLjZiY', 'password_question_100045', 'answer_of_password_question_100045', '4335 Jasmin Valley
New Mariahaven, DE 89283', '1-548-704-520', curdate(), 'username_100045');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100046, 'XUdzlyN8RY', 'password_question_100046', 'answer_of_password_question_100046', '646 Devin Ports Suite 643
Michaelberg, CA 72673', '1-355-975-938', curdate(), 'username_100046');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100047, 'fW$I8B&VNN', 'password_question_100047', 'answer_of_password_question_100047', '1856 Green Hollow Suite 535
Port Gregory, MN 06097', '1-750-515-794', curdate(), 'username_100047');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100048, 'D4t2kJ#a%D', 'password_question_100048', 'answer_of_password_question_100048', '0542 Marshall Summit Apt. 314
Port Roberta, NM 57891', '1-739-764-974', curdate(), 'username_100048');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100049, 'hPavfG5R2p', 'password_question_100049', 'answer_of_password_question_100049', '707 Thomas Haven Apt. 920
Anthonyborough, NV 84233', '1-698-596-996', curdate(), 'username_100049');
insert into client (id, ucid, password, password_question, answer_of_password_question, address, phone_number,
                    date_of_registration, username)
VALUES (uuid(), 100050, 'c1gnE!vehh', 'password_question_100050', 'answer_of_password_question_100050', '758 Dylan Trail
Port Jeffrey, CA 62537', '1-815-536-315', curdate(), 'username_100050');

/* add 3 administrators */

insert into administrator (id,
                           password,
                           address,
                           phone_number,
                           date_of_registration,
                           date_of_birth,
                           username,
                           first_name,
                           middle_initial,
                           last_name)
values (uuid(),
        'admin_password_1',
        '79203 Andrea Pass Apt. 701',
        '1-615-446-421',
        curdate(),
        '1993-01-01',
        'admin_1',
        'RICK', 'P', 'TAM');

insert into administrator (id,
                           password,
                           address,
                           phone_number,
                           date_of_registration,
                           date_of_birth,
                           username,
                           first_name,
                           middle_initial,
                           last_name)
values (uuid(),
        'admin_password_2',
        '79203 Andrea Pass Apt. 702',
        '1-615-446-422',
        curdate(),
        '1993-01-02',
        'admin_2',
        'ROBERT', 'L', 'AYERS');

insert into administrator (id,
                           password,
                           address,
                           phone_number,
                           date_of_registration,
                           date_of_birth,
                           username,
                           first_name,
                           middle_initial,
                           last_name)
values (uuid(),
        'admin_password_3',
        '79203 Andrea Pass Apt. 703',
        '1-615-446-423',
        curdate(),
        '1993-01-03',
        'admin_3',
        'MATTHEW', null, 'O\'BRIEN');

/** Insert 3 announcements **/
insert into announcement (id, content, post_date, title)
values (uuid(), 'content_1', curdate(), 'title_1');
insert into announcement (id, content, post_date, title)
values (uuid(), 'content_2', curdate(), 'title_2');
insert into announcement (id, content, post_date, title)
values (uuid(), 'content_3', curdate(), 'title_3');

/** Insert 20 items **/
delete
from seller;
delete
from item;

insert into seller (client_id)
values ((select id from client where ucid = 100000));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_0', 'item_description_0', 146.00, 'used_good', 'books',
        (select id from client where ucid = 100000));
insert into seller (client_id)
values ((select id from client where ucid = 100001));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_1', 'item_description_1', 134.00, 'new', 'consumer_electronics',
        (select id from client where ucid = 100001));
insert into seller (client_id)
values ((select id from client where ucid = 100002));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_2', 'item_description_2', 72.00, 'used_very_good', 'video_games',
        (select id from client where ucid = 100002));
insert into seller (client_id)
values ((select id from client where ucid = 100003));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_3', 'item_description_3', 132.00, 'used_good', 'personal_computers',
        (select id from client where ucid = 100003));
insert into seller (client_id)
values ((select id from client where ucid = 100004));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_4', 'item_description_4', 198.00, 'used_very_good', 'software',
        (select id from client where ucid = 100004));
insert into seller (client_id)
values ((select id from client where ucid = 100005));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_5', 'item_description_5', 197.00, 'used_acceptable', 'electronic_books',
        (select id from client where ucid = 100005));
insert into seller (client_id)
values ((select id from client where ucid = 100006));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_6', 'item_description_6', 104.00, 'used_good', 'musical_instrument',
        (select id from client where ucid = 100006));
insert into seller (client_id)
values ((select id from client where ucid = 100007));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_7', 'item_description_7', 155.00, 'new', 'video_games',
        (select id from client where ucid = 100007));
insert into seller (client_id)
values ((select id from client where ucid = 100008));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_8', 'item_description_8', 110.00, 'used_good', 'consumer_electronics',
        (select id from client where ucid = 100008));
insert into seller (client_id)
values ((select id from client where ucid = 100009));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_9', 'item_description_9', 104.00, 'used_open_box', 'books',
        (select id from client where ucid = 100009));
insert into seller (client_id)
values ((select id from client where ucid = 100010));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_10', 'item_description_10', 163.00, 'new', 'office_products',
        (select id from client where ucid = 100010));
insert into seller (client_id)
values ((select id from client where ucid = 100011));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_11', 'item_description_11', 97.00, 'used_open_box', 'office_products',
        (select id from client where ucid = 100011));
insert into seller (client_id)
values ((select id from client where ucid = 100012));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_12', 'item_description_12', 172.00, 'used_very_good', 'office_products',
        (select id from client where ucid = 100012));
insert into seller (client_id)
values ((select id from client where ucid = 100013));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_13', 'item_description_13', 105.00, 'used_acceptable', 'others',
        (select id from client where ucid = 100013));
insert into seller (client_id)
values ((select id from client where ucid = 100014));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_14', 'item_description_14', 88.00, 'used_acceptable', 'musical_instrument',
        (select id from client where ucid = 100014));
insert into seller (client_id)
values ((select id from client where ucid = 100015));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_15', 'item_description_15', 150.00, 'new', 'personal_computers',
        (select id from client where ucid = 100015));
insert into seller (client_id)
values ((select id from client where ucid = 100016));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_16', 'item_description_16', 51.00, 'used_good', 'others',
        (select id from client where ucid = 100016));
insert into seller (client_id)
values ((select id from client where ucid = 100017));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_17', 'item_description_17', 61.00, 'used_good', 'software',
        (select id from client where ucid = 100017));
insert into seller (client_id)
values ((select id from client where ucid = 100018));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_18', 'item_description_18', 131.00, 'used_acceptable', 'software',
        (select id from client where ucid = 100018));
insert into seller (client_id)
values ((select id from client where ucid = 100019));
insert into item (id, name, description, price, `condition`, type, client_id_of_seller)
values (uuid(), 'item_name_19', 'item_description_19', 64.00, 'new', 'food',
        (select id from client where ucid = 100019));

/*Fancy stuff */
/*Fancy Announcement*/
insert into announcement (id, content, post_date, title) values
(uuid(), 'After a couple of months planning, we are delighted to announce the launch of our newly online shopping website. We wanted this website to better collaborate with our fellow students and teachers.
We hope you like the fresh new look to the website, and the navigation that will allow you to find useful items much easier and in a cheaper price. Please enjoy a safer purchase!', curdate(), 'Welcome!');

insert into announcement (id, content, post_date, title) values
(uuid(), 'Since our website only allow University of Calgary Members to make oders, you will only buy or sell items to people who is currently a student or teacher in UC. We even allow people to purchase through their Unicard, you do not even to worry about all those credit card fraud. In general, it is a much safer space than other online purchasing platforms.', curdate(), 'Why safer?');


insert into announcement (id, content, post_date, title) values
(uuid(), 'Can I use this website if I am not a student of UC?  Answer: NO you can not, but you can ask a friend who is an UC member to help you to buy or sell whatever you want or you might want to consider to tansfer to UC', curdate(), 'Common Questions');

insert into announcement (id, content, post_date, title) values
(uuid(), 'If you have any problem while using the website, please feel feel to contact us. (We are available every day from 7:00 a.m. until 10:00 p.m. MT.)', curdate(), 'Contact us');
