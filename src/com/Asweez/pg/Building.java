package com.Asweez.pg;

public abstract class Building {
	public Empire empire;
	public Person owner;
	
	public Building(Empire e, Person p){
		empire = e;
		this.owner = p;
	}
	
	public abstract void onTurn();
}
