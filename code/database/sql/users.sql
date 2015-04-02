drop table users   if exists;
drop table address if exists;
drop table lives   if exists;

create table users (
  uid bigint auto_increment,
  fname varchar(25),
  lname varchar(25),
  password varchar(15),
  age int,
  primary key (uid)
);

create table address (
  aid bigint auto_increment,
  street varchar(100),
  city varchar(50),
  state varchar(2),
  zipcode varchar(9),
  primary key (aid)
);

create table lives (
  uid int,
  aid int,
  foreign key (uid) references users on delete cascade,
  foreign key (aid) references address on delete cascade,
  unique(uid,aid)
);

insert into users (fname, lname, password, age) values
  ('Aiden', 'Hall', 'eeee', 19),
  ('Caleb', 'Manu', 'dddd', 7),
  ('Hazel', 'Nutting', 'cccc', 4),
  ('Veb', 'Nordhagen', 'bbbb', 30),
  ('Bill', 'Flood', 'aaaa', 29),
  ('Jane', 'Doe', 'yyyy', 28),
  ('John', 'Doe', 'xxxx', 27);

insert into address (street, city, state, zipcode) values
  ('1 mallard drive', 'cambridge', 'MA', '34567'),
  ('21 jump street', 'new york', 'NY', '98765'),
  ('4 cherry lane', 'truffala', 'NJ', '58235'),
  ('16 strong road', 'chutney', 'VT', '38573'),
  ('99 livingston circle', 'amherst', 'MA', '99822'),
  ('1123 main street', 'worcester', 'MA', '22234');

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '1123 main street'
    and  fname  = 'Aiden';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '16 strong road'
    and  fname  = 'Caleb';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '1123 main street'
    and  fname  = 'Hazel';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '21 jump street'
    and  fname  = 'Veb';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '99 livingston circle'
    and  fname  = 'Bill';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '1 mallard drive'
    and  fname  = 'Jane';

insert into lives(uid, aid)
  select users.uid, address.aid
  from   users, address
  where  street = '4 cherry lane'
    and  fname  = 'John';
