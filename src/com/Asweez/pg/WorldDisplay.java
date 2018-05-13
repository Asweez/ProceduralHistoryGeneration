package com.Asweez.pg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WorldDisplay extends JComponent implements ListSelectionListener{
	
	private World world;
	private JList<Empire> empireList;
	private JScrollPane scrollPane;
	
	public WorldDisplay(World world){
		this.world = world;
		empireList = new JList<Empire>(world.empires.toArray(new Empire[world.empires.size()]));
		empireList.addListSelectionListener(this);
		scrollPane = new JScrollPane(empireList);
		add(scrollPane);
		scrollPane.setLocation(0, 100);
		scrollPane.setSize(300, 100);
		setPreferredSize(new Dimension(300, 200));
	}
	
	Font bold = new Font("Times New Roman", Font.BOLD, 30);
	Font normal = new Font("Times New Roman", Font.PLAIN, 15);
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(bold);
		g.drawString("World", 15, 30);
		g.setFont(normal);
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		PersonalityGeneration.eDisp.e = empireList.getSelectedValue();
		PersonalityGeneration.eDisp.list.setListData(empireList.getSelectedValue().populace.toArray(new SignificantPerson[empireList.getSelectedValue().populace.size()]));
		PersonalityGeneration.empireFrame.revalidate();
		PersonalityGeneration.empireFrame.repaint();
		PersonalityGeneration.mapFrame.repaint();
	}
	
	public Empire getSelectedEmpire(){
		return empireList.getSelectedValue();
	}
}
