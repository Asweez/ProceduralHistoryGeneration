package com.Asweez.pg;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JComponent;

public class PersonalityDisplay extends JComponent{

	public SignificantPerson p;
	public String[] labels = new String[]{"Bravery", "Agressiveness", "Strength", "Intelligence", "Charisma", "Greed"};
	public Color[] colors = new Color[]{new Color(1f, 0.8f, 0), Color.red, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.DARK_GRAY};
	
	public PersonalityDisplay(SignificantPerson p){
		this.p = p;
	}
	
	@Override
	public void paint(Graphics g) {
		if(p == null){
			return;
		}
		float[] values = p.personality;
		g.drawString(p.name, 15, 15);
		g.drawString(p.race.toString(), 15, 30);
		int centerX = 250;
		int centerY = 250;
		int radius = 180;
		int pointRadius = 4;
		double radiansBetweenPoints = (2*Math.PI)/values.length;
		int offset = 20;
		for(Trait t : p.traits){
			g.drawString(t.name + ": " + t.tooltip, 15, centerY + radius + offset);
			offset += 15;
		}
//		g.drawString("Happiness", x, y);
		
		g.drawOval(centerX - (int)(radius), centerY - (int)(radius),radius*2, radius * 2);
		g.setColor(colors[0]);
		int prevX = centerX + (int)(radius * values[0]);
		int prevY = centerY;
		//the 2s are to show the before race-modifier stats (base stats, pure RNG)
		int prevX2 = centerX + (int)(radius * Math.pow(values[0], p.race.modifiers[0]));
		int prevY2 = centerY;
		g.fillPolygon(new int[]{prevX, centerX + (int)(Math.cos(radiansBetweenPoints * (values.length - 1)) * radius * values[values.length - 1]), centerX}, new int[]{prevY, centerY + (int)(Math.sin(radiansBetweenPoints * (values.length - 1)) * radius * values[values.length - 1]), centerY}, 3);
//		g.fillPolygon(new int[]{prevX2, centerX + (int)(Math.cos(radiansBetweenPoints * (values.length - 1)) * radius * Math.pow(values[values.length - 1], p.race.modifiers[values.length - 1])), centerX}, new int[]{prevY2, centerY + (int)(Math.sin(radiansBetweenPoints * (values.length - 1)) * radius * Math.pow(values[values.length - 1], p.race.modifiers[values.length - 1])), centerY}, 3);

		for(int i = 1; i < values.length; i++){
			g.setColor(colors[i]);
			int xPos = centerX + (int)(Math.cos(radiansBetweenPoints * (i)) * radius * values[i]);
			int yPos = centerY + (int)(Math.sin(radiansBetweenPoints * (i)) * radius * values[i]);
			int xPos2 = centerX + (int)(Math.cos(radiansBetweenPoints * (i)) * radius * Math.pow(values[i], p.race.modifiers[i]));
			int yPos2 = centerY + (int)(Math.sin(radiansBetweenPoints * (i)) * radius * Math.pow(values[i], p.race.modifiers[i]));
			g.fillPolygon(new int[]{xPos, prevX, centerX}, new int[]{yPos, prevY, centerY}, 3);
//			g.setColor(new Color(0, 0, 0, 0.5f));
//			g.fillPolygon(new int[]{xPos2, prevX2, centerX}, new int[]{yPos2, prevY2, centerY}, 3);
			prevX = xPos;
			prevY = yPos;
			prevX2 = xPos2;
			prevY2 = yPos2;
		}
		for(int i = 0; i < values.length; i++){
			g.setColor(colors[i]);
			int xPos = centerX + (int)(Math.cos(radiansBetweenPoints * (i)) * radius * values[i]);
			int yPos = centerY + (int)(Math.sin(radiansBetweenPoints * (i)) * radius * values[i]);
			g.fillOval(xPos - pointRadius, yPos - pointRadius, pointRadius * 2, pointRadius * 2);
			g.drawString(labels[i], centerX + (int)(Math.cos(radiansBetweenPoints * (i)) * (radius + 0.25f)), centerY + (int)(Math.sin(radiansBetweenPoints * (i)) * (radius + 0.25f)));
		}
	}
	
	public static Color randomColor(int seed, float alpha){
		Random rand = new Random(seed);
		return new Color(rand.nextFloat() * 0.8f, rand.nextFloat() * 0.8f, rand.nextFloat() * 0.8f, alpha);
	}
	
	public static Color randomColor(int seed){
		return randomColor(seed, 1);
	}
	
	public void changePerson(SignificantPerson p){
		this.p = p;
		repaint();
	}
	
}
