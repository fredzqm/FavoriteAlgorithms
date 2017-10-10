package stringSearch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BoyerMooreMatcher {
	private String searchFor;
	private Map<Character, Integer> shiftTable;
	private int[] matchShiftTable;

	public BoyerMooreMatcher(String searchFor) {
		this.searchFor = searchFor;
		this.shiftTable = new HashMap<>();
		for (int i = 1; i < searchFor.length(); i++) {
			char c = searchFor.charAt(searchFor.length() - 1 - i);
			if (!shiftTable.containsKey(c)) {
				shiftTable.put(c, i);
			}
		}
		this.matchShiftTable = new int[searchFor.length()];
		for (int i = 0; i < searchFor.length(); i++) {
			for (int shift = 1; shift < searchFor.length(); shift++) {
				int matchStart = searchFor.length()-i;
				int shiftStart = Math.max(0, matchStart-shift);
				int len = searchFor.length()-shift-shiftStart;
				if (match(searchFor, shiftStart, searchFor.length()-shift, searchFor, searchFor.length()-len)) {
					matchShiftTable[i] = shift;
					break;
				}
			}
		}
		System.out.println(Arrays.toString(matchShiftTable));
	}

	private boolean match(String a, int ai, int aj, String b, int bi) {
		for (int i = ai; i < aj; i++) {
			if (a.charAt(i) != b.charAt(i-ai+bi))
				return false;
		}
		return true;
	}
	
	private int missMatch(char c) {
		return shiftTable.getOrDefault(c, searchFor.length());
	}
	
	private int gootMatch(int i) {
		return matchShiftTable[i];
	}
	
	public Iterator<Integer> findMatchIn(CharSequence searchIn) {
		return new Matcher(searchIn);
	}

	class Matcher implements Iterator<Integer> {
		private CharSequence searchIn;
		private int index;
		private Integer nextMatchStart;

		public Matcher(CharSequence searchIn) {
			this.searchIn = searchIn;
			this.index = searchFor.length() - 1;
			search();
		}

		private void search() {
			search: while (true) {
				System.out.println(index + " " + searchIn);
				if (index >= searchIn.length()) {
					this.nextMatchStart = null;
					return;
				}
				for (int i = 0; i < searchFor.length(); i++) {
					if (searchIn.charAt(index - i) != searchFor.charAt(searchFor.length() - 1 - i)) {
						index += Math.max(missMatch(searchIn.charAt(index - i))-i, gootMatch(i));
						continue search;
					}
				}
				System.out.println("found: "+index);
				nextMatchStart = index - searchFor.length() + 1;
				index++;
				break;
			}
		}

		@Override
		public boolean hasNext() {
			return nextMatchStart != null;
		}

		@Override
		public Integer next() {
			Integer ret = nextMatchStart;
			search();
			return ret;
		}
	}
}
