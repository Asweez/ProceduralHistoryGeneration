package com.Asweez.pg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EmpireDisplay extends JComponent implements MouseListener, ListSelectionListener{
	
	public Empire e;
	
	public EmpireDisplay(Empire e) {
		this.e = e;
		addMouseListener(this);
		
		
		list = new JList<SignificantPerson>(e.populace.toArray(new SignificantPerson[e.populace.size()]));
		list.addListSelectionListener(this);
		scrollpane = new JScrollPane(list);
		add(scrollpane);
		scrollpane.setLocation(0, 200);
		scrollpane.setSize(300, 200);
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setPreferredSize(new Dimension(300, 500));
	}
	
	Font bold = new Font("Times New Roman", Font.BOLD, 30);
	Font normal = new Font("Times New Roman", Font.PLAIN, 15);
	
	private JScrollPane scrollpane;
	public JList<SignificantPerson> list;
	
	@Override
	public void paint(Graphics g) {
		g.setFont(bold);
		g.drawString(e.name, 15, 30);
		g.setFont(normal);
		g.drawString(e.race.name(), getWidth() - g.getFontMetrics().stringWidth(e.race.name()) - 10, 30);
		g.drawString("Leader: " + e.leader.name, 15, 50);
		
	
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		int y = m.getY();
		if(isNear(y, 50, 5)){
			PersonalityGeneration.pDisp.changePerson(e.leader);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public boolean isNear(int i1, int i2, int bound){
		return Math.abs(i1 - i2) <= bound;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		PersonalityGeneration.pDisp.changePerson(list.getSelectedValue());
	}
}
