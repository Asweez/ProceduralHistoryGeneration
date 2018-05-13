package com.Asweez.pg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Empire {
	public Race race;
	public List<SignificantPerson> populace;
	public SignificantPerson leader;
	public int wealth;
	public Language language;
	public String name;
	public int size;
	public World world;
	public double taxes = 0;
	public int food = 0;
	public Coordinate location;
	public List<Building> buildings;

	public Empire(Race r, World w) {
		race = r;
		world = w;
		language = w.languages.get(race);
		name = language.getCityName();
		populace = new ArrayList<SignificantPerson>();
//		Random rand = new Random();
//		for (int i = 0; i < 20; i++) {
//			populace.add(new SignificantPerson(rand.nextInt(6) == 0 ? Race.getRandom() : r, this));
//		}
//		populace.sort(new PersonComparator());
//		buildings  = new ArrayList<Building>();
//		findLeader();
//		findPlaceToSettle();
	}

	public void findPlaceToSettle() {
		Coordinate settlingLocation = new Coordinate(0, 0);
		double maxScore = Integer.MIN_VALUE;
		Coordinate startingPos = world.getRandomLandCoordinate();
		for (int i = -100; i < 100; i++) {
			for (int j = -100; j < 100; j++) {
				Coordinate c = new Coordinate(startingPos.x + i, startingPos.y + j);
				if (world.isCoordinateValid(c) && world.isLand(c)) {
					double thisScore = evaluateSettlingLocation(c);
					if (thisScore > maxScore) {
						maxScore = thisScore;
						settlingLocation = c;
					}
				}
			}
		}
		location = settlingLocation;
	}

	public double evaluateSettlingLocation(Coordinate c) {
		double score = 0;
		Biome b = world.getBiome(c.x, c.y);
		if(world.elevation[c.x][c.y] < world.waterLevel){
			return Integer.MIN_VALUE;
		}
		if (race.preferredBiome != null && b.equals(race.preferredBiome)) {
			score += 3;
		}
		for (Empire e : world.empires) {
			if (e.location != null) {
				double dist = c.sqrDist(e.location);
				score += dist/10000;
			}
		}
		score += world.iron[c.x][c.y];
		score += world.copper[c.x][c.y];
		score += world.coal[c.x][c.y];
		return score;
	}

	public void findLeader() {
		float leadershipmax = 0;
		SignificantPerson leader = populace.get(0);
		for (SignificantPerson p : populace) {
			float leadership = p.personality[2] + p.personality[4];
			if (leadership > leadershipmax) {
				leadershipmax = leadership;
				leader = p;
			}
		}
		this.leader = leader;
		taxes = leader.personality[5] * leader.personality[5];
	}

	public void onTurn(){
		
	}
	
	@Override
	public String toString() {
		return name;
	}

	static class PersonComparator implements Comparator<SignificantPerson> {

		@Override
		public int compare(SignificantPerson o1, SignificantPerson o2) {
			return o1.name.compareTo(o2.name);
		}

	}
}
