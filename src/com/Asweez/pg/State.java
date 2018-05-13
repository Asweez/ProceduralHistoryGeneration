//package com.Asweez.pg;
//
//import static com.Asweez.pg.EmpireActionManager.*;
//
//public enum State {
//
//	NOT_HUNGRY(EatFood),
//	HAS_WEAPON(),
//	HAS_FOOD(Hunt, HarvestFarm),
//	HAS_FARM(),
//	FARM_READY(Wait);
//
//	static{
//		HAS_FARM.setActions(createBuildingAction(BuildingFarm.class, HAS_FARM));
//	}
//	
//	public Action[] possibleActions;
//
//	State(Action... possibleActions) {
//		this.possibleActions = possibleActions;
//	}
//	
//	State(){
//		this.possibleActions = new Action[]{};
//	}
//	
//	public void setActions(Action... actions){
//		possibleActions = actions;
//	}
//}
