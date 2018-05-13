package com.Asweez.pg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SignificantPerson extends Person{
	public float[] personality;
	public List<Trait> traits;
	//Just a quick and easy world reference
	private World world;
	
	public Job job;
	
	public SignificantPerson(Person p){
		super();
		world = p.empire.world;
		personality = new float[6];
		for(int i = 0; i < 6; i++){
			float baseValue = World.rand.nextFloat();
			float modifiedValue = (float) Math.pow(baseValue, 1f/race.modifiers[i]);
			personality[i] = modifiedValue;
		}
		traits = new ArrayList<Trait>();
	}
	
	public SignificantPerson(Race r, Empire e){
		this(new Person());
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
}
