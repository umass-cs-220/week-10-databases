package cs220.users

import scalikejdbc._

private[users] class User(
           val uid: Int,
           val fname: String,
           val lname: String,
           val password: String,
           val age: Int) {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./users", "sa", "")
  implicit val session = AutoSession

  override def toString = s"User($uid, $fname, $lname, $age)"

  def delete: Unit = {
    sql"""
      delete from users
      where uid = ${uid}
    """.update.apply()
  }

  def lives: List[Address] =
    sql"""
     select *
     from   address, users, lives
     where  users.uid = ${uid}
       and  lives.uid = users.uid
       and  lives.aid = address.aid;
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()

  def lives(addr: Address): Unit =
    sql"""
      insert into lives
      values (${uid}, ${addr.aid})
    """.update.apply()
}

object User {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./users", "sa", "")
  implicit val session = AutoSession

  def list: List[User] =
    sql"select * from users;".map(
      rs => new User(rs.int("uid"),
                     rs.string("fname"),
                     rs.string("lname"),
                     rs.string("password"),
                     rs.int("age"))
    ).list.apply()

  def printAll: Unit =
    list.foreach(u =>
      println(s"${u.uid} ${u.fname} ${u.lname} ${u.password} ${u.age}")
    )

  def get(fname: String, lname: String): Option[User] = {
    sql"""
      select *
      from   users
      where  fname = ${fname}
        and  lname = ${lname}
    """.map(
      rs => new User(rs.int("uid"),
                     rs.string("fname"),
                     rs.string("lname"),
                     rs.string("password"),
                     rs.int("age"))
    ).single.apply()
  }

  def make(fname: String, lname: String, password: String, age: Int): User = {
    sql"""
      insert into users (fname, lname, password, age)
      values (${fname}, ${lname}, ${password}, ${age})
    """.update.apply()
    get(fname, lname) match {
      case None    => throw new RuntimeException(s"could not make user $fname $lname")
      case Some(u) => u
    }
  }
}
