package cs220.users

import scalikejdbc._

object Lives {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./users", "sa", "")
  implicit val session = AutoSession

  private def printUser(a: Address): Unit =
    println(s"  ${a.street}, ${a.city}, ${a.state}, ${a.zipcode}")

  private def printUser(u: User): Unit = {
    println(s"\n${u.fname} ${u.lname}")
    u.lives.foreach(printUser)
  }

  def printUser: Unit =
    User.list.foreach(printUser)

  private def printAddress(a: Address): Unit = {
    println(s"\n${a.street}, ${a.city}, ${a.state}, ${a.zipcode}")
    a.lives.foreach(printAddress)
  }

  private def printAddress(u: User): Unit = {
    println(s"  ${u.fname} ${u.lname}")
  }

  def printAddress: Unit =
    Address.list.foreach(printAddress)
}