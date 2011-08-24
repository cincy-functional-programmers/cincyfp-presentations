package org.kyrlach.scala.example5.test

import org.kyrlach.scala.example5.Player

class TestWinningPlayer(override val name:String, override val health:Int) extends Player(name, health) {
  override def initiative() = GameTests.listToStream(List[Int](2,1,2,1,2))
  override def attack() = GameTests.listToStream(List[Int](5,5,5,5,5))
  override def defend() = GameTests.listToStream(List[Int](5,5,5,5,5))
}