package org.kyrlach.scala.example5.test

import org.kyrlach.scala.example5.Player

class TestLosingPlayer(override val name:String, override val health:Int) extends Player(name, health) {
  override def initiative() = GameTests.listToStream(List[Int](1,2,1,2,1));
  override def attack() = GameTests.listToStream(List[Int](1,1,1,1,1))
  override def defend() = GameTests.listToStream(List[Int](1,1,1,1,1))
}