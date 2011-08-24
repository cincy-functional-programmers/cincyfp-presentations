package org.kyrlach.scala.example3

class Game(var player1:Player, var player2:Player) {
	def start():String = {
	  while(player1.health > 0 && player2.health > 0) {
	    if(whoHasInitiative(player1.initiative, player2.initiative) == player1) {
	      System.out.println(doCombatRound(player1, player2));
	    } else {
	      System.out.println(doCombatRound(player2, player1));
	    }
	  }
	  if(player1.health > 0) {
	    player1.name + " was victorius!";
	  } else {
	    player2.name + " was victorius!";
	  }
	}
	
	def whoHasInitiative(f1: () => Int,f2: () => Int):Player = {
	  f1().compareTo(f2()) match {
	    case 1 => player1
	    case 0 => whoHasInitiative(f1,f2)
	    case -1 => player2
	  }
	}
	
	def doCombatRound(attacker:Player, defender:Player):String = {
	  var attack:Int = attacker.attack();
	  var defense:Int = defender.defend();
	  if(attack > defense) {
	    defender.takeWound(attack - defense);
	    attacker.name + " visciously wounds " + defender.name;
	  } else if (attack == defense) {
	    attacker.name + " tried valiantly to injure " + defender.name + " but " + defender.name + "s defense was too strong.";
	  } else {
	    attacker.takeWound(defense - attack);
	    "Despite " + attacker.name + " having the initiative, " + defender.name + " was able to defend the attack and launch a devastating counter-attack.";
	  }
	}
}