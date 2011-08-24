package org.kyrlach.scala.example3

import java.util.Random

class Player(var name:String, var health:Int) {
	var r:Random = new Random();
	
	def initiative():Int = r.nextInt(2) + 1;
	def defend():Int = r.nextInt(5) + 1;
	def attack():Int = r.nextInt(5) + 1;
	def takeWound(damage:Int):Unit = health -= damage; 
}