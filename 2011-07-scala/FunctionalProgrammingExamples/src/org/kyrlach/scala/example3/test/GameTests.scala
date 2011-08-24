package org.kyrlach.scala.example3.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.kyrlach.scala.example3.Player
import org.kyrlach.scala.example3.Game

class GameTests {
	var player1:Player = _
	var player2:Player = _
	
	var game:Game = _
	
	var f1: () => Int = _
	var f2: () => Int = _

	@Before 
	def setUp():Unit = {
		player1 = new Player("Ben", 10);
		player2 = new Player("Wes", 10);
		game = new Game(player1, player2);
		f1 = () => 1
		f2 = () => 2
		
	}
	
	@Test
	def testPlayer1Initiative():Unit = {
	  assertTrue(game.whoHasInitiative(f2,f1) == player1)
	}
	
	@Test
	def testPlayer2Initiative():Unit = {
	  assertTrue(game.whoHasInitiative(f1,f2) == player2)	 
	}
	
	@Test
	def testInitiativeTie():Unit = {
	  assertTrue(game.whoHasInitiative(f1,f1) == player2)
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