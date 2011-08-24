package org.kyrlach.scala.example2.test

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert._
import org.kyrlach.scala.example2.Player

class PlayerTests {
  
	var player:Player = _
	
	@Before 
	def setUp():Unit = {
		player = new Player("Ben", 10);
	}
	
	@Test 
	def testInitiative():Unit = {
		val initiative = player.initiative(); 
		assertTrue(initiative > 0 && initiative < 3);
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