package com.Asweez.pg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.Asweez.pg.EmpireActionManager.Action;
import com.Asweez.pg.EmpireActionManager.StateVisit;

public class Person {
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
	public List<State> states;
	public List<Action> actions;
	public List<State> goals;

	public Person(String name, Race race, Empire empire) {
		this.name = name;
		this.race = race;
		this.empire = empire;
		this.buildings = new ArrayList<Building>();
		this.actions = new ArrayList<Action>();
		this.goals = new ArrayList<State>();
		this.states = new ArrayList<State>();
		for (int i = 0; i < skills.length; i++) {
			skills[i] = World.rand.nextDouble();
		}
	}

	public void onTurn() {
		hunger -= 0.2f;
		if (hunger <= 0f && !goals.contains(State.NOT_HUNGRY)) {
			hunger = 0;
			states.remove(State.NOT_HUNGRY);
			goals.add(State.NOT_HUNGRY);
			StateVisit path = EmpireActionManager.getToState(State.NOT_HUNGRY, this);
			if (path != null) {
				List<Action> newActions = path.visitedActions;
				Collections.reverse(newActions);
				actions.addAll(newActions);
			}
		}
		if (actions.size() > 0) {
			if (actions.get(0).canDoAction(this) && 
					(states.containsAll(Arrays.asList(actions.get(0).prerequisiteStates())) || actions.get(0).prerequisiteStates().length == 0)) {
				Action a = actions.remove(0);
				System.out.println(a);
				a.doAction(this);
			}else{
				actions.clear();
				StateVisit path = EmpireActionManager.getToState(State.NOT_HUNGRY, this);
				if (path != null) {
					List<Action> newActions = path.visitedActions;
					Collections.reverse(newActions);
					actions.addAll(newActions);
				}
				Action a = actions.remove(0);
				System.out.println(a);
				a.doAction(this);
			}
		}
		Iterator<State> iter = goals.iterator();
		while (iter.hasNext()) {
			State s = iter.next();
			if (states.contains(s)) {
				iter.remove();
			}
		}
		for(Building b : buildings){
			if(b instanceof BuildingFarm && !states.contains(State.HAS_FARM)){
				states.add(State.HAS_FARM);
				break;
			}
		}
	}

	public String toString() {
		return name;
	}
}
