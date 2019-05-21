package services

import model._

object TeamService {
  val db = scala.collection.mutable.Map(
    1 -> Team(1, "Team1", List(1, 2)),
    2 -> Team(2, "Team2", List(3)),
  )

  def insert(id: Int, name: String, teamMembers: List[Int]): Unit = if (db.keySet.contains(id)) {
    throw new Exception("A team with this ID already exists!")
  } else if (teamMembers.size > 2) {
    throw new Exception("Cannot have a team with more than 2 members.")
  } else if (teamMembers.isEmpty) {
    throw new Exception("Cannot have a team with fewer than 1 members.")
  } else {
    db += (id -> Team(id, name, teamMembers))
  }

  def delete(id: Int): Unit = if (db.keySet.contains(id)) {
    db -= id
  } else {
    throw new Exception("A team with this ID was not found in the database.")
  }
}