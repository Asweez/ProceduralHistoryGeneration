package com.Asweez.pg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Empire {
	public Race race;
	public List<Person> populace;
	public Person leader;
	public int wealth;
	public Language language;
	public String name;
	public int size;
	public World world;
	public float taxes = 0;
	
	public Empire(Race r, World w){
		race = r;
		world = w;
		language = w.languages.get(race);
		name = language.getCityName();
		populace = new ArrayList<Person>();
		Random rand = new Random();
		for(int i = 0; i < 20; i++){
			populace.add(new Person(rand.nextInt(6) == 0 ? Race.getRandom() : r, this));
		}
		populace.sort(new PersonComparator());
		findLeader();
	}
	
	public void findLeader(){
		float leadershipmax = 0;
		Person leader = populace.get(0);
		for(Person p : populace){
			float leadership = p.personality[2] + p.personality[4];
			if(leadership > leadershipmax){
				leadershipmax = leadership;
				leader = p;
			}
		}
		this.leader = leader;
		taxes = leader.personality[5] * leader.personality[5];
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	static class PersonComparator implements Comparator<Person>{

		@Override
		public int compare(Person o1, Person o2) {
			return o1.name.compareTo(o2.name);
		}
		
	}
}
