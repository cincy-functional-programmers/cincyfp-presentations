package org.kyrlach.scala.example1

class Game(player1:Player, player2:Player) {
	def start():Unit = {
	  while(player1.health > 0 && player2.health > 0) {
	    var initiative1:Int = 0;
	    var initiative2:Int = 0;
	    while(initiative1 == initiative2) {
	      initiative1 = player1.initiative();
	      initiative2 = player2.initiative();
	    }
	    if(initiative1 > initiative2) {
	      doCombatRound(player1, player2);
	    } else {
	      doCombatRound(player2, player1);
	    }
	  }
	  if(player1.health > 0) {
	    System.out.println(player1.name + " was victorius!");
	  } else {
	    System.out.println(player2.name + " was victorius!");
	  }
	}
	
	def doCombatRound(attacker:Player, defender:Player):Unit = {
	  var attack:Int = attacker.attack();
	  var defense:Int = defender.defend();
	  if(attack > defense) {
	    defender.takeWound(attack - defense);
	    System.out.println(attacker.name + " visciously wounds " + defender.name);
	  } else if (attack == defense) {
	    System.out.println(attacker.name + " tried valiantly to injure " + defender.name + " but " + defender.name + "s defense was too strong.");
	  } else {
	    attacker.takeWound(defense - attack);
	    System.out.println("Despite " + attacker.name + " having the initiative, " + defender.name + " was able to defend the attack and launch a devastating counter-attack.");
	  }
	}
}