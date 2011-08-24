package org.kyrlach.scala.example5

import java.io.{OutputStreamWriter,Writer}

object Game {
  
  def play(p1:Player, p2:Player):Player = {
    doCombat(p1,p2,p1.attack().zip(p1.defend()), p2.attack().zip(p2.defend()), whoHasInitiative(p1,p2), new OutputStreamWriter(System.out))
  }
  
  def whoHasInitiative(p1:Player, p2:Player):Stream[Int] = {
    p1
      .initiative()
      .zip(
        p2
        .initiative()
      )
      .map(
        x => x._1.compareTo(x._2)
      )
      .filter(
        c => c != 0  
      )
      .map(
        y => y match {
          case 1 => 1
          case -1 => 2
        }
      )
  }
  
  def doCombat(p1:Player, p2:Player, p1CombatData:Stream[(Int,Int)], p2CombatData:Stream[(Int,Int)], initiativeData:Stream[Int], output:Writer):Player = {
    if(initiativeData.isEmpty || p1CombatData.isEmpty || p2CombatData.isEmpty) {
      p1
    } else {
      if(p1.health <= 0) {
        p2
      } else if (p2.health <= 0) {
        p1
      } else {
	    initiativeData.head match {
	      case 1 =>
	        p1CombatData.head._1.compareTo(p2CombatData.head._2) match {
	          case 1 =>
	            output.write(p1.name + " visciously wounds " + p2.name)
	            doCombat(p1, p2.takeWound(p1CombatData.head._1 - p2CombatData.head._2), p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	          case 0 =>
	            output.write(p1.name + " tried valiantly to injure " + p2.name + " but " + p2.name + "s defense was too strong.")
	            doCombat(p1, p2, p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	          case -1 =>
	            output.write("Despite " + p1.name + " having the initiative, " + p2.name + " was able to defend the attack and launch a devastating counter-attack.")
	            doCombat(p1.takeWound(p2CombatData.head._2 - p1CombatData.head._1), p2, p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	        }
	      case 2 =>
	        p2CombatData.head._1.compareTo(p1CombatData.head._2) match {
	          case 1 =>
	            output.write(p2.name + " visciously wounds " + p1.name)
	            doCombat(p1.takeWound(p2CombatData.head._1 - p1CombatData.head._2), p2, p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	          case 0 =>
	            output.write(p2.name + " tried valiantly to injure " + p1.name + " but " + p1.name + "s defense was too strong.")
	            doCombat(p1, p2, p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	          case -1 =>
	            output.write("Despite " + p2.name + " having the initiative, " + p1.name + " was able to defend the attack and launch a devastating counter-attack.")
	            doCombat(p1, p2.takeWound(p1CombatData.head._2 - p2CombatData.head._1), p1CombatData.tail, p2CombatData.tail, initiativeData.tail, output)
	        }	        
	    }
      }
    }
  }
  
}