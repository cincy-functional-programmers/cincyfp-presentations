package org.kyrlach.scala.example4.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.kyrlach.scala.example4.Player
import org.kyrlach.scala.example4.Game
import org.kyrlach.scala.example4.RandomSequence

object GameTests {
  def listToStream[A](l:List[A]):Stream[A] = Stream.cons( l.head , if(l.length == 1) { Stream.Empty } else { listToStream(l.tail) });
}

class GameTests {
	var player1:Player = _
	var player2:Player = _
	
	var game:Game = _
	
	var s1:Stream[Int] = _
	var s2:Stream[Int] = _
	
	@Before 
	def setUp():Unit = {
		player1 = new Player("Ben", 10);
		player2 = new Player("Wes", 10);
		game = new Game(player1, player2);
		s1 = GameTests.listToStream(List[Int](1,1,2,1)) 
		s2 = GameTests.listToStream(List[Int](1,1,2,2))
	}
	
	@Test
	def testPlayer1Initiative():Unit = {
	  assertTrue(game.whoHasInitiative(s2,s1) == 1)
	  assertTrue(game.betterWhoHasInitiative(s2,s1) == 1)
	}
	
	@Test
	def testPlayer2Initiative():Unit = {
	  assertTrue(game.whoHasInitiative(s1,s2) == 2)
	  assertTrue(game.betterWhoHasInitiative(s1,s2) == 2)
	}
	
	@Test
	def testPlayer2WinsForTie():Unit = {
	  assertTrue(game.whoHasInitiative(s1,s1) == 2)
	  assertTrue(game.betterWhoHasInitiative(s1,s1) == 1)
	}
	
	@Test
	def testPlayer2WinsForEmpty():Unit = {
	  assertTrue(game.whoHasInitiative(Stream.Empty,Stream.Empty) == 2)
	  assertTrue(game.betterWhoHasInitiative(Stream.Empty,Stream.Empty) == 1)
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