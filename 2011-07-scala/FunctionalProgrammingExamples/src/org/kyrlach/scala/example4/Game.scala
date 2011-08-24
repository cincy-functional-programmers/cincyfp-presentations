package org.kyrlach.scala.example4

class Game(var player1:Player, var player2:Player) {
	def start():String = {
	  while(player1.health > 0 && player2.health > 0) {
	    if(whoHasInitiative(player1.initiative, player2.initiative) == 1) {
	      System.out.println(doCombatRound(player1, player2))
	    } else {
	      System.out.println(doCombatRound(player2, player1))
	    }
	  }
	  if(player1.health > 0) {
	    player1.name + " was victorius!"
	  } else {
	    player2.name + " was victorius!";
	  }
	}
	
	def whoHasInitiative(s1:Stream[Int],s2:Stream[Int]):Int = {
	  if(s1.isEmpty) {
	    2
	  } else if (s2.isEmpty) {
	    1
	  } else {
		  s1.head.compareTo(s2.head) match {
		    case 1 => 1
		    case 0 => whoHasInitiative(s1.tail, s2.tail)
		    case -1 => 2
		  }
	  }
	}
	
	def betterWhoHasInitiative(s1:Stream[Int], s2:Stream[Int]):Int = {
	  s1
	    .zip(s2)
	    .map(p => p._1.compareTo(p._2))
	    .map( x => if(x == -1) { 2 } else { x })
	    .filter( c => c != 0)
	    .headOption
	    .getOrElse(1)
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