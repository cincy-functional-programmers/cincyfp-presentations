package org.kyrlach.java.example1;

import scala.Console;

public class NonFunctional {
	public static void main(String[] args) {
		System.out.print("Please tell me your name for the records? ");
		Player player1 = new Player(Console.readLine(), 10);
		System.out.print("And what, pray tell, is the name of your worthy opponent? ");
		Player player2 = new Player(Console.readLine(), 10);
		Game game = new Game(player1, player2);
		game.start();		
	}
}
