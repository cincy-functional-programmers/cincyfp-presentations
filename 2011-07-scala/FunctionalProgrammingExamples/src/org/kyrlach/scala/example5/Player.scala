package org.kyrlach.scala.example5

import java.util.Random

class Player(val name:String, val health:Int) {
	def initiative():Stream[Int] = RandomSequence(2).map(_ + 1);
	def defend():Stream[Int] = RandomSequence(5).map(_ + 1);
	def attack():Stream[Int] = RandomSequence(5).map(_ + 1);
	def takeWound(damage:Int):Player = new Player(name, health - damage); 
}