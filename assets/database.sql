create database udpserver;

use udpserver;

create table users(
   
   id int(11) not null auto_increment PRIMARY KEY ,
   username varchar(50) not null ,
   password varchar(50) not null
   
);

create online_users(

  id int(11) not null auto_increment PRIMARY KEY ,
  user_id int(11) not null ,
  ip_address varchar(20) ,
  port_no int(11)
  
);