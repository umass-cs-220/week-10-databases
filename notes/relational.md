# Notes on Relational Databases

## Overview

A relational database is one that is composed of *relations*. A
relation is simply a *table* consisting of *rows* and *columns*. A row
is an *instance* in the table and the column defines *attributes* of
the row. For example, here is a relational table for a simple `User`
table:

```
fname      lname      password     age
---------------------------------------
John       Doe        john5432     27
Jane       Doe        jane9876     28
```

The relational database is one that has been around for several
decades and has made a dramatic impact in how we manage and store
data - especially large amounts of data ("big data"). It is also a
very *structured* approach. Tables are precisely described before hand
and data is copied into the database in the format provided by its
definition. *PostgreSQL*, *Oracle*, *MySQL*, *h2*, and *sqlite* are
all examples of traditional relational databases.

## Relational Database Basics

We will begin our exploration of relational databases by looking at
how to manipulate an existing database with the *structured query
language* (SQL). SQL is a language designed to define and query
relational databases. SQL was developed by IBM (System R) in the 1970s
and was standardized over time to accommodate the many database
vendors that adopted the relational model. The SQL standard is
supported by all relational databases, but often is extended to
provide additional functionality related to the particular system.

SQL is composed of two important parts:

- **Data Definition Language** (DDL): Focuses on the creation,
  altering, and deletion of database tables.
- **Data Manipulation Language** (DML): Focuses on the querying and
  modification of a data base instance.

A relational database system may manage several database instances. A
database instance contains *tables* where each table is composed of
*fields* or *attributes*. A field is contains data such as a first
name, last name, age, etc. Each field has a type that defines what
that field is allowed to hold. A table is populated by rows where each
row contains the data for the corresponding fields.

## DML Structure Query Language (SQL)

### The Basics

The most important SQL operations that you want to perform revolve
around the SQL `select` statement. Here are a few examples:

```sql
select * from users;
```

This will select all of the attributes of all the rows in the `users`
table. You should see a result like this:

```
uid   | fname |   lname   | password | age
------+-------+-----------+----------+-----
1     | Aiden | Hall      | eeee     |  19
2     | Caleb | Manu      | dddd     |   7
3     | Hazel | Nutting   | cccc     |   4
4     | Veb   | Nordhagen | bbbb     |  30
5     | Bill  | Flood     | aaaa     |  29
6     | Jane  | Doe       | yyyy     |  28
7     | John  | Doe       | xxxx     |  27
(7 rows, 12 ms)
```

Although this is interesting, it becomes more useful when we wish to
select particular attributes that we care about. For example, how
might we pose the query *get all user ages?*

```sql
select age from users;
```

In this case, the results we get back are:

```
age
-----
27
28
29
30
4
7
19
(7 rows)
```

What if we want the last name and ages for all users?

```sql
select age, lname from users;
```

Then our result looks like this:

```
age  |   lname
-----+-----------
27   | Doe
28   | Doe
29   | Flood
30   | Nordhagen
4    | Nutting
7    | Manu
19   | Hall
(7 rows)
```

How would we retrieve the age and last name for all users over 29? We
can do this by extending the above query with the *where* clause to
provide a *qualification condition*:

```sql
select age, lname
from users
where age > 29;
```

The results are not surprising:

```
age  |   lname
-----+-----------
30   | Nordhagen
(1 row)
```

The basic SQL query can be described generally as:

```
SELECT  [DISTINCT] target-list
FROM    relation-list
WHERE   qualification
```

The *target-list* is a list of attributes, the *relation-list* is a
list of relations, the *qualification* are comparisons using `<`, `>`,
`=`, `<=`, `>=`, `<>`, and can be combined with `AND`, `OR`, and
`NOT`. `DISTINCT` is an optional keyword indicating that the answer
should not contain duplicates. The default is that duplicates are not
eliminated. For number types the relational operators have the usual
meaning. For *character* and *variable length character* types the
ordering is *lexicographic*. For *date* and *time* types they operate
as you would expect. We will cover types when we discuss the DDL part
of SQL.

You can also perform pattern matching on string-like data by using the
**`like`** operator. The `like` operator has the basic format: `s like
p` where `p` may contain two special symbols:

- `%` : matches any sequence of characters.
- `_` : matches any single character

Here is an example of how you might use the `like` operator:

```sql
select *
from   users
where  fname like 'V%b';
```

This will return the results:

```
fname |   lname   | password | age
------+-----------+----------+-----
Veb   | Nordhagen | bbbb     |  30
(1 row)
```

#### Distinct

The underlying model for relational databases uses a set. Queries are
operations over these sets. A set implies that there are no
duplicates. In practice, however, it might be important to support
duplicate results from a query. We can used the `distinct` keyword to
accomplish this:

```sql
select distinct lname
from   users;
```

In this case, if multiple last names are in the result set they
`distinct` will eliminate the duplicates.

#### Ordering Results

It is often important to be able to order the results of a query. To
do this in SQL we will extend the general query to include a `order
by` clause:

```sql
select *
from   users
where  age > 21
order by lname, age;
```

In the above example we are getting all the users that are over 21 and
ordering the results by their last name followed by their
age. Executing this query will give the following result:

```
fname |   lname   | password | age
-------+-----------+----------+-----
John  | Doe       | xxxx     |  27
Jane  | Doe       | yyyy     |  28
Bill  | Flood     | aaaa     |  29
Veb   | Nordhagen | bbbb     |  30
(4 rows)
```

### Multiple Tables

In the relational model *tables* are flat. That is, they do not
support *nested* tables, columns, or rows. In fact, to properly model
your data in a relational database requires you to decompose your data
model into multiple tables where each focuses on a particular *entity*
(think of this as an object) that you need to represent.

Imagine we want to maintain information about a user's address. One
option would be to include a *street*, *city*, *state*, and *zipcode*
as new attributes of the `users` table. This might work out fine, but
consider the case where we have multiple users that live at the same
residence. If we were to use this approach we would end up with
duplicate data which could lead to unnecessary overhead.

A better approach would be to used a separate table called `address`
with the attributes *street*, *city*, *state*, and *zipcode*. In order
to link an address to a user we can include an additional attribute
*`uid`* in the `users` table and include an additional attribute
*`aid`* in the `address` table. The `uid` attribute will be used to
identify a user uniquely in the `users` table and the `aid` will be
used to uniquely identify an address in the `address` table. We can
then create a separate table called `lives` with two attributes: `uid`
and `aid`. The `lives` table will be used to link a user with an
address. The `uid` attribute and the `aid` attribute in the `users`
and `address` tables respectively are referred to as **primary
keys**. That is, they uniquely identify each row in the corresponding
tables. Those same attributes used in the `lives` table are referred
to as **foreign keys**. We can use these to provide uniqueness
constraints in a single table and as references from one table to another.

### Joins
What happens if we need to get at information that is the combination
of two or more tables?

*get the first name, last name, and city of all users over 21*

```sql
select U.fname, U.lname, A.city
from address A, users U, lives L
where U.age > 21
and U.uid = L.uid
and A.aid = L.aid;
```

Result:

```
fname |   lname   |   city
------+-----------+-----------
John  | Doe       | worcester
Jane  | Doe       | chutney
Bill  | Flood     | worcester
Veb   | Nordhagen | new york
(4 rows)
```

*get the first, last name, and street of all the users who live at the
same address order by last name descending*

```sql
select U.fname, U.lname, A.street
from address A, users U, users O, lives LU, lives LO
where U.uid = LU.uid
and O.uid = LO.uid
and A.aid = LU.aid
and A.aid = LO.aid
and U.uid <> O.uid
order by U.lname desc;
```

The results:

```
fname | lname |      street
------+-------+------------------
Bill  | Flood | 1123 main street
John  | Doe   | 1123 main street
(2 rows)
```

### Renaming Columns

It can be useful to rename columns to help the calling application:

```sql
select fname as first, lname as last
from users;
```

The results:

```
first |   last
------+-----------
John  | Doe
Jane  | Doe
Bill  | Flood
Veb   | Nordhagen
Hazel | Nutting
Caleb | Manu
Aiden | Hall
(7 rows)
```

Renaming columns is also very useful when using the *aggregate functions*.

### Aggregation Operators
SQL supports several *aggregation operators* that perform basic
computation or summarization. To aggregate means to form parts into a
whole: when we aggregate values we combine them into a smaller number
of values in a meaningful way. Here is a table of the aggregate
operators provided by SQL:

- count(*)
- count([distinct] A)
- sum([distinct] A)
- avg([distinct] A)
- max(A)
- min(A)

#### Count

*get the count of all the users who are over 21*

```sql
select count(*) as over_21_count
from users
where age > 21;
```

The result:

```
over_21_count
---------------
4
(1 row)
```

Count applies to duplicates. If we wanted to eliminate duplicates we
can used the `distinct` keyword:

```sql
select count(distinct lname)
from users;
```

The result would be:

```
count
-------
6
(1 row)
```

If we did not use the `distinct` keyword the result would be `7`.

### Average

*get the average age*

```sql
select avg(age) as average_age
from users;
```

The result:

```
average_age
---------------------
20.5714285714285714
(1 row)
```

### Group By and Having

We often want to apply aggregates to each of a number of groups. For
example, what if we wanted to know the minimum age of users grouped by
city?

```sql
select A.city, min(U.age)
from users U, address A, lives L
where U.uid = L.uid
and A.aid = L.aid
group by A.city;
```

### NULL Values

SQL can talk about the absence of a value. This value is referred to
as a *NULL* value. NULL can be a value in a column when the value for
that attribute is *unknown*. An *unknown* value can be useful in
certain circumstances. It can also lead to problems in queries:

```sql
select *
from users
where age > 4 and age < 65;
```

If an age were unknown - we would exclude a user in this query.

SQL also provides operators that allow for an explicit test of the
NULL value:

- is null
- is not null

## DDL Structure Query Language (SQL)

The data definition language part of SQL provides statements for
creating tables with attributes. There are many different data types
supported by the SQL language. Because we are not focused on studying
SQL in its entirety we will use only a few data types (`bigint`,
`int`, `varchar`) to work with. For full coverage of relational
databases and structured query language consider taking a course on
relational databases!

The basic structure of creating a table in SQL is:

```sql
create table users (
  uid bigint,
  fname varchar(25),
  lname varchar(25),
  password varchar(15),
  age int,
  primary key (uid)
);
```

In the above SQL code we are creating a table called `users` with
fields representing information that we are interested in keeping on
users. Note, that we do not include information about where a user
lives - this is left to other tables that maintain that
information. The above definition also includes a `primary key` on the
`uid` field. A primary key is used to uniquely identify a user in the
`users` table and it is used in other tables as a reference.

Here is another definition for a table storing address information:

```sql
create table address (
  aid bigint,
  street varchar(100),
  city varchar(50),
  state varchar(2),
  zipcode varchar(9),
  primary key (aid)
);
```

Again, this table is only used to store information about addresses
and **not** information on who lives where. We delegate that task to
yet a third table which will "join" the other two tables together on
who lives where:

```sql
create table lives (
  uid int,
  aid int,
  foreign key (uid) references users,
  foreign key (aid) references address,
  unique(uid,aid)
  );
```

In the `lives` table we designate the `uid` and `aid` as *foreign
keys* to the referenced tables `users` and `address`. By designating
`uid` and `aid` as foreign keys we can ensure data consistency, that
is, the rows contains in our `lives` table will only contain rows that
reference valid rows in the `users` and `address` table.

To add data to the tables above we can use the SQL `insert` statement:

```sql
insert into users values (1, 'Aiden', 'Hall', 'eeee', 19);
insert into users values (2, 'Caleb', 'Manu', 'dddd', 7);
insert into users values (3, 'Hazel', 'Nutting', 'cccc', 4);
insert into users values (4, 'Veb', 'Nordhagen', 'bbbb', 30);
insert into users values (5, 'Bill', 'Flood', 'aaaa', 29);
insert into users values (6, 'Jane', 'Doe', 'yyyy', 28);
insert into users values (7, 'John', 'Doe', 'xxxx', 27);
```

Again, for the `address` table:

```sql
insert into address values (1, '1 mallard drive', 'cambridge', 'MA', '34567');
insert into address values (2, '21 jump street', 'new york', 'NY', '98765');
insert into address values (3, '4 cherry lane', 'truffala', 'NJ', '58235');
insert into address values (4, '16 strong road', 'chutney', 'VT', '38573');
insert into address values (5, '99 livingston circle', 'amherst', 'MA', '99822');
insert into address values (6, '1123 main street', 'worcester', 'MA', '22234');
```

Yet again, for the `lives` table:

```sql
insert into lives values
  (1, 6),
  (2, 4),
  (3, 6),
  (4, 2),
  (5, 5),
  (6, 1),
  (7, 3);
```
