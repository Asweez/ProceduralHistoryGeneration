package com.Asweez.pg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class GOAPAgent {
	public HashMap<String, Object> states;
	public HashMap<String, GOAPAction> availableActions;
	public List<GOAPGoal> goals;

	public GOAPAgent() {
		states = new HashMap<>();
		availableActions = new HashMap<>();
		goals = new ArrayList<GOAPGoal>();
	}

	public void initStates(String... strings) {
		for (String s : strings) {
			String[] keyValue = s.split("=");
			// Condition
			states.put(keyValue[0], parseValue(keyValue[1]));
		}
	}

	public List<String> plan(GOAPGoal goal) {
		GOAPStateNode startingNode = new GOAPStateNode();
		GOAPStateNode bestNode = null;
		startingNode.incompleteStates.putAll(goal.conditions);
		Iterator<Entry<String, Object>> iter = startingNode.incompleteStates.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> e = iter.next();
			if (states.containsKey(e.getKey()) && states.get(e.getKey()).equals(e.getValue())) {
				iter.remove();
			}
		}
		if (startingNode.incompleteStates.size() == 0)
			return startingNode.actions;
		List<GOAPStateNode> queue = new ArrayList<GOAPStateNode>();
		queue.add(startingNode);
		while (!queue.isEmpty()) {
			GOAPStateNode thisNode = queue.get(0);
//			System.out.println(thisNode);
			queue.remove(thisNode);
			outer: for (Entry<String, GOAPAction> actionEntry : availableActions.entrySet()) {
				GOAPAction action = actionEntry.getValue();
				boolean validAction = false;
				for (Entry<String, Object> actionEffect : action.effectStates.entrySet()) {
					if (thisNode.incompleteStates.containsKey(actionEffect.getKey()) && thisNode.incompleteStates.get(actionEffect.getKey()).equals(actionEffect.getValue())) {
						validAction = true;
						break;
					}
				}
				if(!validAction || (bestNode != null && thisNode.cost + action.cost >= bestNode.cost)) continue outer;
				GOAPStateNode newNode = new GOAPStateNode(thisNode.incompleteStates, thisNode.actions, thisNode.cost + action.cost);
				for (Entry<String, Object> actionEffect : action.effectStates.entrySet()) {
					newNode.incompleteStates.remove(actionEffect.getKey(), actionEffect.getValue());
				}
				for (Entry<String, Object> actionCondition : action.conditionStates.entrySet()) {
					if (!states.entrySet().contains(actionCondition)) {
						newNode.incompleteStates.put(actionCondition.getKey(), actionCondition.getValue());
					}
				}
				newNode.actions.add(actionEntry.getKey());
				if (newNode.incompleteStates.size() == 0) {
					if(bestNode == null || newNode.cost < bestNode.cost){
						bestNode = newNode;
					}
				} else {
					queue.add(newNode);
					Collections.sort(queue);
//					System.out.println("Queue: " + queue);
				}
			}
//			System.out.print("");
		}
		return bestNode == null ? null : bestNode.actions;
	}

	private static class GOAPStateNode implements Comparable<GOAPStateNode> {
		public HashMap<String, Object> incompleteStates;
		public List<String> actions;
		public int cost = 0;

		public GOAPStateNode(HashMap<String, Object> incompleteStates, List<String> actions, int cost) {
			this.incompleteStates = new HashMap<String, Object>(incompleteStates);
			this.actions = new ArrayList<String>(actions);
			this.cost = cost;
		}

		public GOAPStateNode() {
			incompleteStates = new HashMap<>();
			actions = new ArrayList<String>();
		}

		@Override
		public int compareTo(GOAPStateNode o) {
			return Integer.compare(cost + actions.size(), o.cost + o.actions.size());
		}

		@Override
		public String toString() {
			return Arrays.toString(actions.toArray());
		}
	}

	public static class GOAPAction {
		public HashMap<String, Object> conditionStates;
		public HashMap<String, Object> effectStates;
		public int cost = 1;

		public GOAPAction(String... strings) {
			effectStates = new HashMap<>();
			conditionStates = new HashMap<>();
			for (String s : strings) {
				if (s.split(">").length > 1) {
					// Effect
					String[] keyValue = s.split(">");
					effectStates.put(keyValue[0], parseValue(keyValue[1]));
				} else {
					String[] keyValue = s.split("=");
					// Condition
					conditionStates.put(keyValue[0], parseValue(keyValue[1]));
				}
			}
		}

	}

	public static class GOAPGoal {
		public HashMap<String, Object> conditions;

		public GOAPGoal(String... strings) {
			conditions = new HashMap<>();
			for (String s : strings) {
				String[] keyValue = s.split("=");
				conditions.put(keyValue[0], parseValue(keyValue[1]));
			}
		}

		public boolean goalCompleted(GOAPAgent agent) {
			for (Entry<String, Object> e : conditions.entrySet()) {
				if (!agent.states.containsKey(e.getKey()) || !agent.states.get(e.getKey()).equals(e.getValue())) {
					return false;
				}
			}
			return true;
		}
	}

	private static Object parseValue(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return Boolean.parseBoolean(value);
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
		}
		return value;
	}
}
