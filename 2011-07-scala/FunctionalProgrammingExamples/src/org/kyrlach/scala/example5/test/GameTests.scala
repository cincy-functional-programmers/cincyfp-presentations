package org.kyrlach.scala.example5.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.kyrlach.scala.example5.Player
import org.kyrlach.scala.example5.Game
import org.kyrlach.scala.example5.RandomSequence

import java.io.StringWriter

object GameTests {
  def listToStream[A](l:List[A]):Stream[A] = Stream.cons( l.head , if(l.length == 1) { Stream.Empty } else { listToStream(l.tail) });
}

class GameTests {
	var player1:Player = _
	var player2:Player = _
	
	var output:StringWriter = _
	
	@Before 
	def setUp():Unit = {
		player1 = new TestWinningPlayer("Ben", 10);
		player2 = new TestLosingPlayer("Wes", 10);
		output = new StringWriter
	}
	
	@Test
	def testPlayer1Initiative():Unit = {
	  assertTrue(Game.whoHasInitiative(player1, player2).count(_ == 1) == 3)
	}
	
	@Test
	def testPlayer2Initiative():Unit = {
	  assertTrue(Game.whoHasInitiative(player1, player2).count(_ == 2) == 2)
	}
	
	@Test
	def testWinner():Unit = {
	  assertTrue(Game.play(player1,player2) == player1)
	}
	
	@Test
	def testDoCombat():Unit = {	  
	  assertTrue(Game.doCombat(player1, player2, player1.attack().zip(player1.defend()), player2.attack().zip(player2.defend()), Game.whoHasInitiative(player1, player2), output) == player1)
	}
}