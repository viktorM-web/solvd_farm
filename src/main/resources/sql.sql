create database if not exists farm;

use farm;

create table if not exists users (
  id serial primary key,
  login varchar(45) not null unique,
  password VARCHAR(45) not null,
  role VARCHAR(45) not null
);

insert into users(login, password, role)
values  ('admin', 1111, 'ADMIN'),
		('salesman', 1111, 'SALESMAN'),
		('farmer1', 1111, 'FARMER'),
		('farmer2', 1111, 'FARMER');

  create table if not exists shops(
  id serial primary key,
  name varchar(45) not null unique,
  user_id bigint unsigned,
  constraint fk_shop_user foreign key (user_id) references users(id) on update no action on delete no action
 );

insert into shops(name, user_id)
values  ('shop1', 1);

 create table if not exists farms(
  id serial primary key,
  name varchar(45) not null unique,
  badget double unsigned not null,
  user_id bigint unsigned,
  constraint fk_farm_user foreign key (user_id) references users(id) on update no action on delete no action
 );

insert into farms(name, badget, user_id)
values  ('Sunrise', 2500.70, 3),
		('Byrenka', 35000.80, 4);


create table if not exists offers (
  id serial primary key,
  type varchar(45) not null,
  description varchar(45) not null,
  price double unsigned not null,
  shop_id bigint unsigned,
  constraint fk_offer_shop foreign key (shop_id) references shops(id) on update no action on delete no action
);

insert into offers(type, description, price, shop_id)
values  ('BUY', 'EGG', 1.05, 1),
		('BUY', 'MILK', 1.45, 1),
		('BUY', 'MEAT', 2.055, 1),
        ('SEL', 'PIG', 100.33, 1),
        ('SEL', 'COW', 420.51, 1),
		('SEL', 'CHICKEN', 10.21, 1),
        ('SEL', 'FOR_PIG', 22.33, 1),
        ('SEL', 'FOR_COW', 55.51, 1),
		('SEL', 'FOR_CHICKEN', 5.21, 1);


create table if not exists animals (
  id serial primary key,
  type varchar(45) not null,
  age int not null,
  weight double unsigned not null,
  farm_id bigint unsigned,
  constraint fk_animal_farm foreign key (farm_id) references farms(id) on update no action on delete no action
);

insert into animals(type, age, weight, farm_id)
values  ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 1),
		('COW', 6, 50.2, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.3, 1),
		('COW', 6, 49.31, 1),
		('CHICKEN', 6, 1.1, 1),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.02, 2),
		('COW', 6, 50.2, 2),
		('CHICKEN', 6, 1.1, 2),
        ('PIG', 6, 25.3, 2),
		('COW', 6, 49.31, 2),
		('CHICKEN', 6, 1.1, 2);

create table if not exists feeds (
  id serial primary key,
  type varchar(45) not null,
  count double unsigned not null,
  farm_id bigint unsigned,
  constraint fk_feed_farm foreign key (farm_id) references farms(id) on update no action on delete no action
);

insert into feeds(type, count, farm_id)
values  ('FOR_PIG', 255.0, 1),
		('FOR_COW', 240.5, 1),
		('FOR_CHICKEN', 360.5, 1),
        ('FOR_PIG', 402.0, 2),
		('FOR_COW', 555.2, 2),
		('FOR_CHICKEN', 500.3, 2);

create table if not exists items (
  id serial primary key,
  type varchar(45) not null,
  count double unsigned not null,
  farm_id bigint unsigned,
  constraint fk_item_farm foreign key (farm_id) references farms(id) on update no action on delete no action
);

insert into items(type, count, farm_id)
values  ('EGG', 55.0, 1),
		('MILK', 40.5, 1),
		('MEAT', 560.5, 1),
        ('EGG', 102.0, 2),
		('MILK', 55.2, 2),
		('MEAT', 500.3, 2);
