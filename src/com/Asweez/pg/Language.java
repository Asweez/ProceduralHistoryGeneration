package com.Asweez.pg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Language {
	
	private Random rand = new Random();
	
	public char[] consonants = "ptkbdgmnszhjw".toCharArray();

	public char[] liquids = "lr".toCharArray();

	public char[] finals = "mndpb".toCharArray();

	public char[] vowels = "aeiou".toCharArray();
	
	public char[] sibilants = "s∑f".toCharArray();

	public String[] morphemes;
	public String[] cityMorphemes;
	public String[] landMorphemes;
	public String[] nameMorphemes;
	public String[] commonSurnames;
	public String[] intermediaries;

	public String format = "C?LV?VF?V";
	
	public int syllablesPerWordMin = 1;
	public int syllablesPerWordMax = 2;
	
	public Language(String format){
		this.format = format;
		initializeLanguage();
	}
	
	public void initializeLanguage(){
		Random rand = new Random();
		int i = rand.nextInt(consonants.length - 1) + 1;
		consonants[i] = consonants[i - 1];
		i = rand.nextInt(vowels.length - 1) + 1;
		vowels[i] = vowels[i - 1];
		i = rand.nextInt(finals.length - 1) + 1;
		finals[i] = finals[i - 1];
		generateMorphemes();
	}
	
	public void generateMorphemes() {
		morphemes = createMorphemeSet(rand.nextInt(4) + 3);
		cityMorphemes = createMorphemeSet(rand.nextInt(3) + 2);
		landMorphemes = createMorphemeSet(rand.nextInt(3) + 2);
		nameMorphemes = createMorphemeSet(rand.nextInt(3) + 2);
		commonSurnames = new String[12];
		intermediaries = new String[3];
		String[] lastNameMorphemes = createMorphemeSet(7);
		for (int i = 0; i < commonSurnames.length; i++) {
			commonSurnames[i] = capitalize(getWord(lastNameMorphemes));
		}
		for (int i = 0; i < intermediaries.length; i++) {
			intermediaries[i] = createSyllable("CVF");
		}
	}
	
	public String[] createMorphemeSet(int num){
		String[] morphemes = new String[num];
		for (int i = 0; i < num; i++) {
			morphemes[i] = generateMorpheme();
		}
		return morphemes;
	}

	private String generateMorpheme() {
		return createSyllable("C?LVC");
	}
	
	public String getMorpheme(){
		return pick(morphemes);
	}
	
	public String getFirstName(){
		return capitalize(getWord(nameMorphemes));
	}
	
	public String getLastName(){
		return commonSurnames[rand.nextInt(commonSurnames.length)];
	}
	
	public String getWholeName(){
		return getFirstName() + " " + getLastName();
	}

	public String getCityName() {
		String name = capitalize(getWord(cityMorphemes));
		if (rand.nextInt(10) < 2) {
			name += " " + pick(intermediaries);
			name += " " + capitalize(getWord(cityMorphemes));
		}
		return name;
	}

	public String getWord(String[] morphemes) {
		int length = rand.nextInt(syllablesPerWordMax - syllablesPerWordMin) + syllablesPerWordMin;
		String word = "";
		List<String> unusedMorphemes = new ArrayList<String>(Arrays.asList(morphemes));
		for (int i = 0; i < length; i++) {
			word += createSyllable(format);
			if (unusedMorphemes.size() > 0 && rand.nextInt(10) < 4) {
				word += unusedMorphemes.remove(rand.nextInt(unusedMorphemes.size()));
			}
		}
		word.replaceAll("∑", "sh");
		if (word.toCharArray().length < 5 || word.toCharArray().length > 12) {
			return getWord(morphemes);
		} else {
			StringBuilder sb = new StringBuilder(word);
			Set<Integer> toRemove = new HashSet<Integer>();
			for (int i = 1; i < word.toCharArray().length; i++) {
				if(toRemove.contains(i))
					continue;
				if (word.toCharArray()[i] == word.toCharArray()[i - 1]) {
					toRemove.add(i);
				}else if(i < word.toCharArray().length - 1){
					List<Character> vList = Arrays.asList('a', 'e', 'i', 'o', 'u');
					if(!vList.contains(word.toCharArray()[i]) && !vList.contains(word.toCharArray()[i - 1]) && !vList.contains(word.toCharArray()[i + 1])){
						toRemove.add(i);
					}
				}
			}
			int i = 0;
			for (Integer integer : toRemove) {
				sb.deleteCharAt(integer - i);
				i++;
			}
			return sb.toString();
		}
	}

	public String getWord() {
		return getWord(morphemes);
	}

	private String createSyllable(String format) {
		String toReturn = "";
		boolean optional = false;
		char[] charArray = format.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == '?') {
				optional = true;
			} else {
				if (optional) {
					optional = false;
					if (rand.nextBoolean()) {
						continue;
					}
				}
				if (c == 'C') {
					toReturn += pick(consonants);
				} else if (c == 'L') {
					toReturn += pick(liquids);
				} else if (c == 'F') {
					toReturn += pick(finals);
				} else if (c == 'V') {
					toReturn += pick(vowels);
				} else if(c == 'S'){
					toReturn += pick(sibilants);
				}
			}
		}
		return toReturn;
	}

	private char pick(char[] array) {
		return array[rand.nextInt(array.length)];
	}

	private <T> T pick(T[] array) {
		return array[rand.nextInt(array.length)];
	}

	private static String capitalize(String s) {
		String first = s.substring(0, 1);
		first = first.toUpperCase();
		return first + s.substring(1);
	}
}
