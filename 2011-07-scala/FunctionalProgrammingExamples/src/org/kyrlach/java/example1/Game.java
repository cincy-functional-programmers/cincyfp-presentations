package org.kyrlach.java.example1;

public class Game {
	private Player player1;
	private Player player2;
	
	public Game(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void start() {
		while(player1.getHealth() > 0 && player2.getHealth() > 0) {
			Integer initiative1 = 0;
			Integer initiative2 = 0;
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
		if(player1.getHealth() > 0) {
			System.out.println(player1.getName() + " was victorius!");
		} else {
			System.out.println(player2.getName() + " was victorius!");
		}		
	}
	
	public void doCombatRound(Player attacker, Player defender) {
		Integer attack = attacker.attack();
		Integer defense = defender.defend();
		if(attack > defense) {
			defender.takeWound(attack - defense);
			System.out.println(attacker.getName() + " visciously wounds " + defender.getName());
		} else if (attack == defense) {
			System.out.println(attacker.getName() + " tried valiantly to injure " + defender.getName() + " but " + defender.getName() + "s defense was too strong.");
		} else {
		    attacker.takeWound(defense - attack);
		    System.out.println("Despite " + attacker.getName() + " having the initiative, " + defender.getName() + " was able to defend the attack and launch a devastating counter-attack.");
		}		    
	}
}
