package com.Asweez.pg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class PersonalityGeneration {
	//Using "BASIC-G" outline
	
	public static void main(String[] args){
		new PersonalityGeneration().main();
	}
	
	public static World world;
	public static Random rand;
	public static PersonalityDisplay pDisp;
	public static EmpireDisplay eDisp;
	public static WorldDisplay wDisp;
	public static MapDisplay mDisp;
	public static JComboBox<String> mapSelectionBox;
	public static JFrame empireFrame, pDispFrame, worldFrame, mapFrame;
	
	public void main(){
		long time1 = System.currentTimeMillis();	
		Thread newThread = new Thread("WorldGen"){
			@Override
			public void run() {
				world = new World(4);
				mDisp = new MapDisplay(world, 1000, 500);
				mapSelectionBox.addItemListener(mDisp);
				mapFrame.add(mDisp, BorderLayout.CENTER);
				mapFrame.pack();
				finishWorldGen();
			}
		};
		newThread.start();
		mapFrame = new JFrame("Map");
		long time = System.currentTimeMillis();
		mapSelectionBox = new JComboBox<String>(new String[]{"Map", "Elevation", "Temperature", "Humidity", "Iron", "Copper", "Coal", "Multicolor"});
		JButton newWorld = new JButton("Generate New");
		newWorld.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mDisp.changeWorld(new World(4));
			}
		});
		mapFrame.add(newWorld, BorderLayout.SOUTH);
		mapFrame.add(mapSelectionBox, BorderLayout.NORTH);
		mapFrame.setLocation(0, 550);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapFrame.setVisible(true);
		mapFrame.pack();
		System.out.println("Frame display took: " + (System.currentTimeMillis() - time) + "ms");
		while(true){
			if(!newThread.isAlive()){
				break;
			}
		}
		System.out.println("OVERALL: " + (System.currentTimeMillis() - time1) + "ms");
	}
	
	public static void finishWorldGen(){
		empireFrame = new JFrame("Empire");
		eDisp = new EmpireDisplay(world.empires.get(0));
		empireFrame.add(eDisp);
		empireFrame.setLocation(520, 0);
		empireFrame.setVisible(true);
		empireFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		empireFrame.pack();
		
		pDispFrame = new JFrame("Person");
		pDispFrame.setLayout(new BoxLayout(pDispFrame.getContentPane(), BoxLayout.Y_AXIS));
		Person p = world.empires.get(0).populace.get(0);
		pDisp = new PersonalityDisplay(p);
		pDispFrame.add(pDisp);
		pDispFrame.setVisible(true);
		pDispFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pDispFrame.setSize(500, 500);
		
		worldFrame = new JFrame("World");
		wDisp = new WorldDisplay(world);
		worldFrame.add(wDisp);
		worldFrame.pack();
		worldFrame.setVisible(true);
		worldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		worldFrame.setLocation(840, 0);
	}
}
