package com.Asweez.pg;

import java.awt.Color;

public enum Biome {
	TROPICAL_RAINFOREST(new Color(0, 0.6f, 0)),
	TEMPERATE_FOREST(new Color(0.5f, 0.7f, 0)),
	DESERT(new Color(0.8455f, 0.765f, 0.1402f)),
	GRASSLAND(Color.green),
	SAVANNA(new Color(1f, 0.56f, 0.36f)),
	TUNDRA(new Color(0.7f, 1f, 1f)),
	TAIGA(new Color(0, 0.6f, 0.6f)),
	ICE(Color.white),
	MOUNTAIN(Color.gray),
	OCEAN(Color.blue),
	EMPTY(Color.black);
	
	public Color biomeColor;
	
	Biome(Color c){
		biomeColor = c;
	}
}
