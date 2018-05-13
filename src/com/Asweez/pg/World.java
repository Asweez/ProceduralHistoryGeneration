package com.Asweez.pg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class World {
	public List<Empire> empires;
	public HashMap<Race, Language> languages;
	public int width = 300;
	public int height = 300;
	public int seed;
	public double waterLevel;
	private int halfWidth, halfHeight;
	//7 0 1
	//6   2
	//5 4 3
	private int windDirection;
	public static Random rand;

	public double[][] elevation, temp, humidity, iron, copper, coal, trees;
	private final boolean exportMap = false;

	public World(int numEmpires) {
		long time = System.currentTimeMillis();
		Random seedgen = new Random();
		seed = seedgen.nextInt();
		System.out.println(seed + ", Width: " + width + ", Height: " + height);
		languages = new HashMap<Race, Language>();
		for (Race r : Race.values()) {
			languages.put(r, new Language("C?L?V?VF?V"));
		}
		halfWidth = (int) (width / 2);
		halfHeight = (int) (height / 2);
		elevation = generateNoiseMap(16, seed);
		//Make island, tweak elevation (weighted sum of squared elevation)
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double dx = i - (width / 2);
				double dy = j - (height / 2);
				double distance = (Math.pow(dx, 2) / Math.pow(halfWidth, 2)) + (Math.pow(dy, 2) / Math.pow(halfHeight, 2));
				double weight = 0.4;
				elevation[i][j] = elevation[i][j] * weight + Math.pow(elevation[i][j], 2) * (1 - weight);
				elevation[i][j] = Math.max(0, elevation[i][j] - Math.pow(distance, 4));
			}
		}
		rand = new Random(seed);
		//Randomize waterLevel
		waterLevel = (rand.nextDouble() * 0.3) + 0.06f;
		waterLevel = 0.3;
		windDirection = rand.nextInt(8);
		int size = 9;
		temp = generateNoiseMap(size, rand.nextInt());
		humidity = generateNoiseMap(size, rand.nextInt());
		iron = generateNoiseMap(size, rand.nextInt());
		copper = generateNoiseMap(size, rand.nextInt());		
		coal = generateNoiseMap(size, rand.nextInt());		
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//Average between random noise and hot in center
				temp[i][j] += 3 * (1 - Math.abs((double) (j - (height / 2)) / (height/2)));
				temp[i][j] /= 4;
				int a = 4;
				double e = elevation[i][j];
				double e1 = e - 0.1;
				double d = (1.5 * (Math.pow(e1, a)) / (Math.pow(e1, a) + Math.pow(1 - e1, a))) + 0.8;
				//Decrease temperatures at higher elevations
				temp[i][j] = Math.pow(temp[i][j], d);
				//Increase humidity at higher temperatures (average between that and noise)
				double h = humidity[i][j];
				double a1 = 1.5;
				humidity[i][j] = Math.min(1, 1.75*h) * temp[i][j];
				if(elevation[i][j] < waterLevel){
					humidity[i][j] = 1;
					iron[i][j] = 0;
					copper[i][j] = 0;
					coal[i][j] = 0;
				}
			}
		}
		System.out.println("World generation took: " + (System.currentTimeMillis() - time) + "ms");
		empires = new ArrayList<Empire>(numEmpires);
		for (int i = 0; i < numEmpires; i++) {
			empires.add(new Empire(Race.getRandom(), this));
		}
	}
	
	private double adjustMoisture(int x, int y){
		double lastHumidity = humidity[x - 1][y - 1];
		double thisHumidity = lastHumidity - Math.pow(elevation[x][y], 6);
		return Math.max(0, thisHumidity);
	}
	
	public double[][] generateNoiseMap(int octaves, int seed) {
		Random rand = new Random(seed);
		FastNoise noise = new FastNoise(rand.nextInt(9999));
		noise.SetFractalGain(0.5f);
		noise.SetFractalLacunarity(2f);
		noise.SetFractalOctaves(octaves);
		noise.SetFrequency(0.005f);
		double[][] toReturn = new double[width][height];
		double max = 0;
		double min = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				toReturn[i][j] = noise.GetValueFractal(i, j);
				if (toReturn[i][j] > max) {
					max = toReturn[i][j];
				}
				if (toReturn[i][j] < min) {
					min = toReturn[i][j];
				}
			}
		}
		double newMax = 0;
		double newMin = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				toReturn[i][j] = (toReturn[i][j] - min) / (max - min);
				if (toReturn[i][j] > newMax) {
					newMax = toReturn[i][j];
				}
				if (toReturn[i][j] < newMin) {
					newMin = toReturn[i][j];
				}
			}
		}
		return toReturn;
	}
	
	public static Biome[][] biomeTable = new Biome[][]{
		{Biome.TUNDRA, Biome.TAIGA, Biome.GRASSLAND, Biome.DESERT, Biome.DESERT},
		{Biome.EMPTY, Biome.GRASSLAND, Biome.GRASSLAND, Biome.GRASSLAND, Biome.DESERT},
		{Biome.EMPTY, Biome.EMPTY, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.SAVANNA},
		{Biome.EMPTY, Biome.EMPTY, Biome.EMPTY, Biome.TROPICAL_RAINFOREST, Biome.TROPICAL_RAINFOREST},
		{Biome.EMPTY, Biome.EMPTY, Biome.EMPTY, Biome.EMPTY, Biome.TROPICAL_RAINFOREST}
	};
	
	public Biome getBiome(int x, int y){
		double e = elevation[x][y];
		if(e < waterLevel){
			return Biome.OCEAN;
		}else if(e > 0.9){
			return Biome.ICE;
		}else if(e > 0.75){
			return Biome.MOUNTAIN;
		}
		return biomeTable[(int) Math.floor(humidity[x][y] / 0.2)][(int) Math.floor(temp[x][y] / 0.2)];
	}
	
	public Coordinate getRandomCoordinate(){
		return new Coordinate(rand.nextInt(width), rand.nextInt(height));
	}
	
	public Coordinate getRandomLandCoordinate(){
		Coordinate c = getRandomCoordinate();
		while(elevation[c.x][c.y] < waterLevel){
			c = getRandomCoordinate();
		}
		return c;
	}
	
	public boolean isCoordinateValid(Coordinate c){
		return c.x >= 0 && c.y >= 0 && c.x < width && c.y < height;
	}
	
	public boolean isLand(Coordinate c){
		return elevation[c.x][c.y] >= waterLevel;
	}
}
