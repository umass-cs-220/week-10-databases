package cs220

import scalikejdbc._
import anorm._

object Stub {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:./simple", "sa", "")

  implicit val session = AutoSession

  def createTable =
    sql"""
    create table members (
      id serial not null primary key,
      name varchar(64),
      created_at timestamp not null
    )
    """.execute.apply()

  def insertMembers =
    Seq("Alice", "Bob", "Chris") foreach { name =>
      sql"insert into members (name, created_at) values (${name}, current_timestamp)"
        .update.apply()

    }

  def entities: List[Map[String, Any]] =
    sql"select * from members".map(_.toMap).list.apply()
}
