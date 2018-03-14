set time_zone = '-5:00';

drop table if exists Users;
create table Users (user VARCHAR(20), pw TINYTEXT, email TINYTEXT, tz varchar(10) default '-5:00', since TIMESTAMP, primary key (user));
insert into Users (user, pw, email) Values ('whand', 'password', 'willishand@gmail.com');
