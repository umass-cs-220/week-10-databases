package cs220.users

import scalikejdbc._

private[users] class Address(val aid: Int,
              val street: String,
              val city: String,
              val state: String,
              val zipcode: String) {

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./users", "sa", "")
  implicit val session = AutoSession

  def delete: Unit = {
    sql"""
      delete from address
      where aid = ${aid}
    """.update.apply()
  }

  def lives: List[User] =
    sql"""
      select *
      from   address, users, lives
      where  address.aid = ${aid}
        and  lives.uid = users.uid
        and  lives.aid = address.aid;
    """.map(
      rs => new User(rs.int("uid"),
                     rs.string("fname"),
                     rs.string("lname"),
                     rs.string("password"),
                     rs.int("age"))
    ).list.apply()

  def lives(u: User): Unit =
    sql"""
      insert into lives
      values (${u.uid}, ${aid})
    """.update.apply()

  override def toString =
    s"Address($aid, $street, $city, $state, $zipcode)"
}

object Address {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./users", "sa", "")
  implicit val session = AutoSession

  def list =
    sql"""
      select * from address
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()

  def printAll: Unit =
    list.foreach(a =>
      println(s"${a.aid} ${a.street} ${a.city} ${a.state} ${a.zipcode}")
    )

  def get(street: String,
          city: String,
          state: String,
          zipcode: String): List[Address] =
    sql"""
      select *
      from   address
      where  street=${street}
        and  city=${city}
        and  state=${state}
        and  zipcode=${zipcode}
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()

  def get(street: String,
          city: String,
          state: String): List[Address] =
    sql"""
      select *
      from   address
      where  street=${street}
        and  city=${city}
        and  state=${state}
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()

  def get(street: String,
          city: String): List[Address] = {
    sql"""
      select *
      from   address
      where  street=${street}
        and  city=${city}
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()
  }

  def get(street: String): List[Address] = {
    sql"""
      select *
      from   address
      where  street=${street}
    """.map(
      rs => new Address(rs.int("aid"),
                        rs.string("street"),
                        rs.string("city"),
                        rs.string("state"),
                        rs.string("zipcode"))
    ).list.apply()
  }


  def make(street: String,
           city: String,
           state: String,
           zipcode: String): List[Address] = {
    sql"""
      insert into address (street, city, state, zipcode)
      values (${street}, ${city}, ${state}, ${zipcode})
    """.update.apply()
    get(street, city, state, zipcode)
  }
}