package com.Asweez.pg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class World {
	public List<Empire> empires;
	public static HashMap<Race, Language> languages;
	public int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public int height = 600;
	public int seed;
	public double waterLevel;
	private int halfWidth, halfHeight;

	public double[][] elevation, temp, humidity, iron, copper;
	private final boolean exportMap = false;

	public World(int numEmpires) {
		long time = System.currentTimeMillis();
		Random seedgen = new Random();
		seed = seedgen.nextInt();
		seed = 618652525;
		System.out.println(seed);
		languages = new HashMap<Race, Language>();
		for (Race r : Race.values()) {
			languages.put(r, new Language("C?L?V?VF?V"));
		}
		halfWidth = (int) (width / 2);
		halfHeight = (int) (height / 2);

		empires = new ArrayList<Empire>(numEmpires);
		for (int i = 0; i < numEmpires; i++) {
			empires.add(new Empire(Race.getRandom(), this));
		}
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
		Random rand = new Random(seed);
		//Randomize waterLevel
		waterLevel = (rand.nextDouble() * 0.2) + 0.08f;
		int size = 9;
		temp = generateNoiseMap(size, rand.nextInt());
		humidity = generateNoiseMap(size, rand.nextInt());
		iron = generateNoiseMap(size, rand.nextInt());
		copper = generateNoiseMap(size, rand.nextInt());		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//Average between random noise and hot in center
				temp[i][j] += 1 * (1 - Math.abs((double) (j - (height / 2)) / (height/2)));
				temp[i][j] /= 2;
				int a = 3;
				double e = elevation[i][j];
				double d = (0.8 * (Math.pow(e, a)) / (Math.pow(e, a) + Math.pow(1 - e, a))) + 0.8;
				temp[i][j] = Math.pow(temp[i][j], d);
				humidity[i][j] += Math.pow(humidity[i][j], Math.pow(e, 2));
				humidity[i][j] /= 2;
				if(elevation[i][j] < waterLevel){
					
				}
			}
		}
		System.out.println("World generation took: " + (System.currentTimeMillis() - time) + "ms");
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
}
