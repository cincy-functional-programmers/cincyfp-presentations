package org.kyrlach.scala.example2.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.kyrlach.scala.example2.Player
import org.kyrlach.scala.example2.Game

class GameTests {
	var player1:Player = _
	var player2:Player = _
	
	var game:Game = _
	
	@Before 
	def setUp():Unit = {
		player1 = new Player("Ben", 10);
		player2 = new Player("Wes", 10);
		game = new Game(player1, player2);
	}
	
	@Test
	def testDoCombat():Unit = {
	  assertTrue(game.doCombatRound(player1, player2).isInstanceOf[String]);
	}
	
	@Test
	def testStart():Unit = {
	  assertTrue(game.start().isInstanceOf[String])
	}
}