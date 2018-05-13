package com.Asweez.pg;

import static com.Asweez.pg.State.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EmpireActionManager {

	public static StateVisit getToState(State s, Person p) {
		LinkedList<StateVisit> queue = new LinkedList<StateVisit>();
		StateVisit initialState = new StateVisit(s, new ArrayList<Action>(), 0);
		StateVisit bestVisit = null;
		// Add lowest cost actions first in queue
		queue.addFirst(initialState);
		while (queue.size() > 0) {
			// Expand the first state in the queue
			StateVisit stateVisit = queue.removeFirst();
			for (Action action : stateVisit.thisState.possibleActions) {
				if (action.canDoAction(p)) {
					List<State> incompletePrereqs = new ArrayList<State>();
					// This is if there are more prerequisites
					for (State prereq : action.prerequisiteStates()) {
						if (!p.states.contains(prereq)) {
							incompletePrereqs.add(prereq);
						}
					}
					if (incompletePrereqs.isEmpty()) {
						// This is if it will be the first action, therefore we
						// have finished searching this tree
						if (bestVisit == null || action.getCost(p) + stateVisit.totalCost < bestVisit.totalCost) {
							StateVisit nextStateVisit = new StateVisit(null, new ArrayList<Action>(stateVisit.visitedActions), action.getCost(p) + stateVisit.totalCost);
							nextStateVisit.visitedActions.add(action);
							bestVisit = nextStateVisit;
						}
					} else if (incompletePrereqs.size() == 1) {
						StateVisit nextStateVisit = new StateVisit(incompletePrereqs.get(0), new ArrayList<Action>(stateVisit.visitedActions), action.getCost(p) + stateVisit.totalCost);
						nextStateVisit.visitedActions.add(action);
						queue.addFirst(nextStateVisit);
					} else {
						StateVisit nextStateVisit = new StateVisit(null, new ArrayList<Action>(stateVisit.visitedActions), action.getCost(p) + stateVisit.totalCost);
						nextStateVisit.visitedActions.add(action);
						boolean flag = true;
						for (int i = 0; i < incompletePrereqs.size(); i++) {
							StateVisit subGoal = getToState(incompletePrereqs.get(i), p);
							if (subGoal == null) {
								flag = false;
								break;
							}
							nextStateVisit.visitedActions.addAll(subGoal.visitedActions);
						}
						if (flag) {
							if (bestVisit == null || action.getCost(p) + stateVisit.totalCost < bestVisit.totalCost) {
								bestVisit = nextStateVisit;
							}
						}
					}
				}
			}
		}
		return bestVisit;
	}

	public static class StateVisit {
		public State thisState;
		public List<Action> visitedActions;
		public double totalCost;

		public StateVisit(State thisState, List<Action> visitedActions, double totalCost) {
			this.thisState = thisState;
			this.visitedActions = visitedActions;
			this.totalCost = totalCost;
		}

	}

	public abstract static class Action {
		public abstract State[] prerequisiteStates();

		public abstract boolean canDoAction(Person p);

		public abstract double getCost(Person p);

		public abstract void doAction(Person p);
	}

	public static Action EatFood = new Action() {

		@Override
		public State[] prerequisiteStates() {
			return new State[] { HAS_FOOD };
		}

		public double getCost(Person p) {
			return 0;
		}

		public boolean canDoAction(Person p) {
			return true;
		}

		@Override
		public void doAction(Person p) {
			p.empire.food--;
			if (p.empire.food <= 0) {
				p.states.remove(HAS_FOOD);
			}
			p.hunger = 1;
			p.states.add(NOT_HUNGRY);
		}

		public String toString() {
			return "Eat Food";
		};

	};
	public static Action Hunt = new Action() {

		@Override
		public State[] prerequisiteStates() {
			return new State[] { HAS_WEAPON };
		}

		@Override
		public boolean canDoAction(Person p) {
			Coordinate c = p.empire.location;
			Biome b = p.empire.world.getBiome(c.x, c.y);
			boolean toReturn = b == Biome.GRASSLAND || b == Biome.TEMPERATE_FOREST || b == Biome.TROPICAL_RAINFOREST;
			return toReturn;
		}

		@Override
		public double getCost(Person p) {
			return 100;
		}

		@Override
		public void doAction(Person p) {
			p.empire.food++;
			p.states.add(HAS_FOOD);
		}

		public String toString() {
			return "Hunt";
		};

	};
	public static Action HarvestFarm = new Action() {

		@Override
		public State[] prerequisiteStates() {
			return new State[] { FARM_READY, HAS_FARM };
		}

		@Override
		public boolean canDoAction(Person p) {
			return true;
		}

		@Override
		public double getCost(Person p) {
			return 1;
		}

		@Override
		public void doAction(Person p) {
			for (Building b : p.buildings) {
				if (b instanceof BuildingFarm && ((BuildingFarm) b).isReadyToHarvest) {
					((BuildingFarm) b).isReadyToHarvest = false;
					p.empire.food += 5;
					p.states.add(HAS_FOOD);
					p.states.remove(FARM_READY);
				}
			}

		}

		public String toString() {
			return "Harvest Farm";
		};

	};
	
	public static Action Wait = new Action() {

		@Override
		public State[] prerequisiteStates() {
			return new State[] { };
		}

		@Override
		public boolean canDoAction(Person p) {
			return true;
		}

		@Override
		public double getCost(Person p) {
			return Integer.MAX_VALUE;
		}

		@Override
		public void doAction(Person p) {


		}

		public String toString() {
			return "Wait";
		};

	};

	public static Action createBuildingAction(Class<? extends Building> c, State toState) {
		return new Action() {

			@Override
			public State[] prerequisiteStates() {
				return new State[] {};
			}

			@Override
			public boolean canDoAction(Person p) {
				return true;
			}

			@Override
			public double getCost(Person p) {
				return 5;
			}

			@Override
			public void doAction(Person p) {
				try {
					Building newBuilding = c.getConstructor(Empire.class, Person.class).newInstance(p.empire, p);
					p.buildings.add(newBuilding);
					p.states.add(toState);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}

			public String toString() {
				return "Build " + c.getName();
			}

		};
	}

}
