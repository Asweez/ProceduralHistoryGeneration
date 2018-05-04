package com.Asweez.pg;

public enum Job {
	BUTCHER(10),
	BLACKSMITH(15),
	NOBLE(30),
	KING(50),
	KNIGHT(35),
	FARMER(5),
	JESTER(20),
	BARD(15),
	MERCHANT(10),
	CARPENTER(10);
	
	public int wealthPerYear;
	
	Job(int wpy){
		wealthPerYear = wpy;
	}
}
