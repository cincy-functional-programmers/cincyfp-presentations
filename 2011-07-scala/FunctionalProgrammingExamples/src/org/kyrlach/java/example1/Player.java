package org.kyrlach.java.example1;

import java.util.Random;

public class Player {
	private Random r = new Random();
	private String name;
	private Integer health;
	
	public Player(String name, Integer health) {
		this.name = name;
		this.health = health;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getHealth() {
		return health;
	}
	public void setHealth(Integer health) {
		this.health = health;
	}

	public Integer initiative() {
		return r.nextInt(2) + 1;
	}
	
	public Integer defend() {
		return r.nextInt(5) + 1;
	}
	
	public Integer attack() {
		return r.nextInt(5) + 1;
	}
	
	public void takeWound(Integer damage) {
		health -= damage;
	}
}
