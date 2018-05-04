package com.Asweez.pg;

import java.util.Random;

public enum Race {

	HUMAN(1, 1, 0.25, 1, 4, 1),
	ELF(1, 0.25, 1, 4, 1, 1),
	GOBLIN(0.25, 1, 1, 1, 1, 4),
	ORC(1, 4, 1, 0.25, 1, 1),
	GIANT(1, 1, 4, 1, 0.25, 1),
	NYMPH(1, 0.25, 1, 1, 1, 0.25),
	DWARF( 4, 1, 1, 0.25, 1, 1);
	
	double[] modifiers;
	
	Race(double b, double a, double s, double i, double c, double g){
		modifiers = new double[]{b, a, s, i, c, g};
	}
	
	public static Race getRandom(){
		return Race.values()[new Random().nextInt(Race.values().length)];
	}
}
