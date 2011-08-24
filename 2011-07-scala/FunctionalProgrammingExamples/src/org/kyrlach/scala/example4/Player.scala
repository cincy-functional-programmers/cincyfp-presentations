package org.kyrlach.scala.example4

import java.util.Random

class Player(var name:String, var health:Int) {
	var r:Random = new Random();
	
	def initiative():Stream[Int] = RandomSequence(2).map(_ + 1);
	def defend():Int = r.nextInt(5) + 1;
	def attack():Int = r.nextInt(5) + 1;
	def takeWound(damage:Int):Unit = health -= damage; 
}