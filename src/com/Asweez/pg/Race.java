package com.Asweez.pg;

import java.util.Random;

public enum Race {

	HUMAN(1, 1, 0.25, 1, 4, 1, Biome.GRASSLAND),
	ELF(1, 0.25, 1, 4, 1, 1, Biome.TROPICAL_RAINFOREST),
	GOBLIN(0.25, 1, 1, 1, 1, 4, null),
	ORC(1, 4, 1, 0.25, 1, 1, null),
	GIANT(1, 1, 4, 1, 0.25, 1, Biome.MOUNTAIN),
	NYMPH(1, 0.25, 1, 1, 1, 0.25, Biome.TEMPERATE_FOREST),
	DWARF( 4, 1, 1, 0.25, 1, 1, Biome.MOUNTAIN);
	
	double[] modifiers;
	public Biome preferredBiome;
	
	Race(double b, double a, double s, double i, double c, double g, Biome biome){
		modifiers = new double[]{b, a, s, i, c, g};
		preferredBiome = biome;
	}
	
	public static Race getRandom(){
		return Race.values()[new Random().nextInt(Race.values().length)];
	}
}
