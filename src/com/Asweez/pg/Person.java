package com.Asweez.pg;

import java.util.ArrayList;
import java.util.List;

public class Person extends GOAPAgent{
	public String name;
	public Race race;
	public Empire empire;
	public int wealth = 10;
	public double hunger = 0;
	public double happiness = 0.5;
	// Farming, Hunting, Mining, Trading, Butchering, Combat, Sailing, Crafting,
	// Deception, Science, Healing
	public double[] skills = new double[11];
	public List<Building> buildings;

	public Person() {

		initStates("hungry=true", "has_farm=false", "has_weapon=true", "has_food=false");
		availableActions.put("Eat", new GOAPAction("has_food=true", "hungry>false", "has_food>false"));
		availableActions.put("Harvest Farm", new GOAPAction("has_farm=true", "has_food>true"));
		availableActions.put("Build Farm", new GOAPAction("has_farm>true"));		
		availableActions.put("Hunt", new GOAPAction("has_weapon=true", "has_food>true"));
		availableActions.get("Hunt").cost = 10;
//		availableActions.get("Build Farm").cost = 10;
		long startTime = System.currentTimeMillis();
		List<String> actions = plan(new GOAPGoal("hungry=false"));
		for(int i = 0; i < 10000; i++){
			actions = plan(new GOAPGoal("hungry=false"));
		}
		System.out.println("Planning took " + (System.currentTimeMillis() - startTime) + "ms");
		for(int i = 0; i < actions.size(); i++){
			System.out.println(actions.get(i));
		}
		System.exit(0);
	}

	public void onTurn() {
		hunger -= 0.2f;
		
	}

	public String toString() {
		return name;
	}
}
