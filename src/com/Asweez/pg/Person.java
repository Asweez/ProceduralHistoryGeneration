package com.Asweez.pg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Person {
	public float[] personality;
	public String name;
	public Race race;
	public List<Trait> traits;
	public Empire empire;
	public World world;
	
	public int wealth;
	public Job job;
	public float happiness;
	
	private static Random rand = new Random();
	
	public Person(Race r, Empire e){
		this.race = r;
		empire = e;
		world = e.world;
		this.name = world.languages.get(race).getWholeName();
		personality = new float[6];
		for(int i = 0; i < 6; i++){
			float baseValue = rand.nextFloat();
			float modifiedValue = (float) Math.pow(baseValue, 1f/race.modifiers[i]);
			personality[i] = modifiedValue;
		}
		traits = new ArrayList<Trait>();
		if(rand.nextBoolean()){
			traits.add(Trait.values()[rand.nextInt(Trait.values().length)]);
			tweakPersonalityFromTraits();
		}
	}

	public void tweakPersonalityFromTraits(){
		for(Trait t : traits){
			switch(t){
			case ALTRUISTIC:
				personality[5] = (float) Math.pow(personality[5], 3);
				break;
			case CONFIDENT:
				personality[0] = (float) Math.pow(personality[0], 0.3);
				break;
			case STRONGARMED:
				personality[2] = (float) Math.pow(personality[2], 0.3);
				break;
			case GEEK:
				personality[3] = (float) Math.pow(personality[3], 0.3);
				personality[4] = (float) Math.pow(personality[4], 3);
				break;
			case PEACEFUL:
				personality[1] = (float) Math.pow(personality[1], 3);
				break;
			case INTROVERT:
				personality[4] = (float) Math.pow(personality[4], 3);
				break;
			case EXTROVERT:
				personality[4] = (float) Math.pow(personality[4], 0.3);
				break;
			case MUSICIAN:
				personality[4] = (float) Math.pow(personality[4], 0.3);
				break;
			case SELFISH:
				personality[5] = (float) Math.pow(personality[5], 0.3);
				break;
			}
		}
	}
	
	public void processYear(){
		wealth += job.wealthPerYear;
	}
	
	public String toString(){
		return name;
	}
}
