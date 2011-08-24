package org.kyrlach.scala.example4.test

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert._
import org.kyrlach.scala.example4.Player

class PlayerTests {
  
	var player:Player = _
	
	@Before 
	def setUp():Unit = {
		player = new Player("Ben", 10);
	}
	
	@Test 
	def testInitiative():Unit = {
		assertTrue(player.initiative().take(1000).filter(c => c < 1 || c > 2).isEmpty);
	}
	
	@Test 
	def testAttack():Unit = {
		val damage = player.attack();
		assertTrue(damage > 0 && damage < 5);
	}
	
	@Test 
	def testDefend():Unit = {
		val defense = player.defend();
		assertTrue(defense > 0 && defense < 6);
	}
	
	@Test 
	def testWound() = {
		player.takeWound(2);
		assertTrue(player.health == 8);
	}
}