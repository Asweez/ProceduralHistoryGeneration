package com.Asweez.pg;

public class Coordinate {
	public int x, y;
	
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	public Coordinate scale(double scale){
		return new Coordinate((int) (x * scale), (int) (y * scale));
	}
	
	public double sqrDist(Coordinate c){
		return Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2);
	}
}
