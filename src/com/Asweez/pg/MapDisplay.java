package com.Asweez.pg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class MapDisplay extends JComponent implements ItemListener {

	public String currentSelection = "Map";
	private World world;
	public int width;
	public int height;
	private double scale = 1f;
	private final boolean shade = true;
	private final boolean exportMap = false;

	public MapDisplay(World world, int width, int height) {
		this.world = world;
		this.width = width;
		this.height = height;
		scale = Math.min((double) width / (double) world.width, (double) height / (double) world.height);
		scale = 1/scale;
		setPreferredSize(new Dimension((int)(world.width/scale), (int)(world.height/scale)));
		generateMapImage();
	}

	private BufferedImage map;

	public void generateMapImage() {
		map = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double[][] waves = generateNoiseMap(3);
		long time = System.currentTimeMillis();
		for (int i = 0; i < width; i++) {
			double maxHeight = 0;
			boolean isOcean = true;

			for (int j = 0; j < height; j++) {
				try{
				double hum = world.humidity[(int) (scale*i)][(int) (scale*j)];
				double temp = world.temp[(int) (scale*i)][(int) (scale*j)];
				double terrain = world.elevation[(int) (scale*i)][(int) (scale*j)];
				Biome b = world.getBiome((int) (scale*i), (int) (scale*j));
				Color c = b.biomeColor;
				if (terrain > 0.9f) {
					c = polar;
				} else if (terrain > 0.75f) {
					c = mountain;
				} else if (terrain < world.waterLevel) {
					c = ocean;
					terrain = world.waterLevel;
				}
				// c = clerp(Color.blue, Color.red,
				// (float)Math.floor(height/0.143)/7f);
				if (shade) {
					if (b == Biome.OCEAN && isOcean) {
						if (waves[(int) i / 5][j] / 10 >= maxHeight) {
							maxHeight = waves[(int) i / 5][j] / 10;
							isOcean = true;
							map.setRGB(i, j, clerp(c, Color.white, (float) (Math.pow(maxHeight, 1.5) * 10)).getRGB());
						} else {
							maxHeight -= 0.004f * scale;
							if (maxHeight < 0) {
								maxHeight = 0f;
							}
							map.setRGB(i, j, c.getRGB());
						}
						continue;
					}
				}
				if (terrain >= maxHeight) {
					if (shade) {
						maxHeight = terrain;
					}
					isOcean = false;
					map.setRGB(i, j, c.getRGB());
				} else {
					maxHeight -= 0.004f * scale;
					if (maxHeight < 0) {
						maxHeight = 0f;
					}
					if (maxHeight < world.waterLevel) {
						isOcean = true;
					}
					map.setRGB(i, j, clerp(c, Color.black, (c.equals(polar) ? 0.2f : 0.4f)).getRGB());
				}
				}catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
		if (exportMap) {
			File file = new File("map_" + world.seed + ".png");
			try {
				file.createNewFile();
				ImageIO.write(map, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Image generation took: " + (System.currentTimeMillis() - time) + "ms");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		currentSelection = PersonalityGeneration.mapSelectionBox.getItemAt(PersonalityGeneration.mapSelectionBox.getSelectedIndex());
		repaint();
	}

	public static Color desert = new Color(0.8455f, 0.765f, 0.1402f);
	public static Color beach = new Color(1f, 0.8f, 0.48f);
	public static Color savanna = new Color(1f, 0.56f, 0.36f);
	public static Color grass = Color.green;
	public static Color forest = new Color(0, 0.6f, 0);
	public static Color temperateForest = new Color(0.5f, 0.7f, 0);
	public static Color mountain = Color.gray;
	public static Color ocean = Color.blue;
	public static Color deepOcean = new Color(0, 0, 0.8f);
	public static Color polar = new Color(1f, 1f, 1f);
	public static Color tundra = new Color(0.7f, 1f, 1f);
	public static Color taiga = new Color(0, 0.6f, 0.6f);

	@Override
	public void paint(Graphics g) {
		if (currentSelection == null)
			return;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		if (currentSelection.equals("Map")) {
			img = map;
		} else if (currentSelection.equals("Multicolor")) {
			int increments = 20;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					img.setRGB(i, j, new Color((float) (Math.floor(world.iron[i][j] * (double) increments) / (double) increments), (float) (Math.floor(world.copper[i][j] * (double) increments) / (double) increments), (float) (Math.floor(world.coal[i][j] * (double) increments) / (double) increments)).getRGB());
				}
			}
		} else {
			Color c1 = Color.black;
			Color c2 = Color.white;
			int increments = 8;
			double[][] data = new double[width][height];
			if (currentSelection.equals("Elevation")) {
				data = world.elevation;
			} else if (currentSelection.equals("Temperature")) {
				increments = 10;
				c1 = Color.blue;
				c2 = Color.red;
				data = world.temp;
			} else if (currentSelection.equals("Humidity")) {
				c1 = Color.black;
				c2 = Color.green;
				data = world.humidity;
			} else if (currentSelection.equals("Iron")) {
				data = world.iron;
			} else if (currentSelection.equals("Copper")) {
				c2 = new Color(0.8f, 0.5f, 0);
				data = world.copper;
			} else if (currentSelection.equals("Coal")) {
				data = world.coal;
			}
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					img.setRGB(i, j, clerp(c1, c2, (float) (Math.floor(data[i][j] * (double) increments) / (double) increments)).getRGB());
				}
			}
		}
		g.drawImage(img, 0, 0, null);
	}

	public static Color clerp(Color c1, Color c2, float f) {
		return new Color((int) lerp(c1.getRed(), c2.getRed(), f), (int) lerp(c1.getGreen(), c2.getGreen(), f), (int) lerp(c1.getBlue(), c2.getBlue(), f));
	}

	public static float lerp(float f1, float f2, float f3) {
		float f = f1 + ((f2 - f1) * f3);
		return f;
	}

	public double[][] generateNoiseMap(int octaves) {
		Random rand = new Random();
		FastNoise noise = new FastNoise(rand.nextInt(9999));
		noise.SetFractalGain(0.5f);
		noise.SetFractalLacunarity(2f);
		noise.SetFractalOctaves(octaves);
		noise.SetFrequency(0.1f);
		double[][] toReturn = new double[world.width][world.height];
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

	public void changeWorld(World newWorld) {
		world = newWorld;
		generateMapImage();
		repaint();
	}

}
