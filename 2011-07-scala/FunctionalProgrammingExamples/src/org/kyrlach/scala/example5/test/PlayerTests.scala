package org.kyrlach.scala.example5.test

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert._
import org.kyrlach.scala.example5.Player

class PlayerTests {
  
	var player:Player = _
	
	@Before 
	def setUp():Unit = {
		player = new Player("Ben", 10);
	}
	
	@Test 
	def testInitiative():Unit = {
	  assertTrue(player.initiative().take(1000).filter(c => c < 1 || c > 2).isEmpty)
	}
	
	@Test 
	def testAttack():Unit = {
	  assertTrue(player.attack().take(10000).filter(c => c < 1 || c > 5).isEmpty)
	}
	
	@Test 
	def testDefend():Unit = {
	  assertTrue(player.attack().take(10000).filter(c => c < 1 || c > 5).isEmpty)
	}
	
	@Test 
	def testWound() = {
	  val woundedPlayer = player.takeWound(2);
	  assertTrue(player.health == 10);
	  assertTrue(woundedPlayer.health == 8)
	}
}