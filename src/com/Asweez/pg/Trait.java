package com.Asweez.pg;

public enum Trait {
	
	IRONMAN("Iron Man", "Much harder to kill"),
	CREATIVE("Creative", "Bursts of intelligence"),
	CONFIDENT("Confident", "Naturally brave"),
	STRONGARMED("Strong Armed", "Very strong"),
	MUSICIAN("Musician", "Musically talented"),
	GEEK("Geek", "Very smart, not too social"),
	HISTORYBUFF("History Buff", "Learns more from the past"),
	AMBITIOUS("Ambitious", "Set out to achieve goals"),
	INSANE("Insane", "Very unpredictable"),
	LAZY("Lazy", "Not very motivated to do much"),
	INTROVERT("Introvert", "Not very social"),
	EXTROVERT("Extrovert", "Very social"),
	MILITARISTIC("Militaristic", "Likes war and fighting"),
	IRRITABLE("Irritable", "Jumps to action quickly"),
	PEACEFUL("Peaceful", "Does not like fighting or war"),
	FAMILYORIENTED("Family Oriented", "Likes to have large families"),
	ALTRUISTIC("Altruistic", "Always has other's needs in mind"),
	GOOD("Good", "Does the right thing"),
	SHY("Shy", "Does not like positions of leadership"),
	NATURALLEADER("Natural Leader", "Naturally drawn to positions of leadership"),
	ECONOMIST("Economist", "Knows how to boost the economy"),
	OFFICIONADO("Officionado", "Makes more money from trading"),
	QUICKLEARNER("Quick Learner", "Learns various traits and skills easily"),
	MASTERMIND("Mastermind", "Knows the best path to victory"),
	PERFECTIONIST("Perfectionist", "Naturally makes the best quality products out there"),
	SELFISH("Selfish", "Look out for number one");
	
	
	public String name, tooltip;
	
	Trait(String name, String tooltip){
		this.name = name;
		this.tooltip = tooltip;
	}
}
