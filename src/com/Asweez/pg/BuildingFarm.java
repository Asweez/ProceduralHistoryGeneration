package com.Asweez.pg;

public class BuildingFarm extends Building {

	public Crop cropType;
	public boolean isReadyToHarvest = false;
	private int timeTilHarvest = 10;

	public BuildingFarm(Empire e, Person p) {
		super(e, p);
		cropType = Crop.values()[World.rand.nextInt(Crop.values().length)];
	}

	@Override
	public void onTurn() {
		if (!isReadyToHarvest) {
			timeTilHarvest--;
			if (timeTilHarvest == 0) {
				System.out.println("Farm Ready");
				timeTilHarvest = 10;
				isReadyToHarvest = true;
				owner.states.add(State.FARM_READY);
			}
		}
	}

	public static enum Crop {
		WHEAT, CARROTS, CORN, POTATOES, BEETS, RICE;
	}
}
