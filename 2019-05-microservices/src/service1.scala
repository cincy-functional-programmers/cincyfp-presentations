package services

import model._

object UserService {
  val db = scala.collection.mutable.Map(
    1 -> User(1, "Ben"),
    2 -> User(2, "Sally"),
    3 -> User(3, "Jim")
  )

  def insert(id: Int, name: String): Unit = if(db.keySet.contains(id)) {
    throw new Exception("A user with this ID already exists!")
  } else {
    db += (id -> User(id, name))
  }

  def delete(id: Int): Unit = if (db.keySet.contains(id)) {
    db -= id
  } else {
    throw new Exception("A user with that ID was not found in the database.")
  }
}