package model

case class User(id: Int, name: String)
case class Team(id: Int, name: String, users: List[Int])