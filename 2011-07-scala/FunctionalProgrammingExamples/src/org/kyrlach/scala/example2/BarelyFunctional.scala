package org.kyrlach.scala.example2

object BarelyFunctional {
  
  def main(args:Array[String]):Unit = {
	  System.out.print("Please tell me your name for the records? ");
	  var player1:Player = new Player(Console.readLine(), 10);
	  System.out.print("And what, pray tell, is the name of your worthy opponent? ");
	  var player2:Player = new Player(Console.readLine(), 10);
	  var game:Game = new Game(player1, player2);
	  System.out.println(game.start());
  }
}