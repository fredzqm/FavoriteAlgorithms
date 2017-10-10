package stringSearch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HorspoolMatcher {
	private String searchFor;
	private Map<Character, Integer> shiftTable;

	public HorspoolMatcher(String searchFor) {
		this.searchFor = searchFor;
		this.shiftTable = new HashMap<>();
		for (int i = 1; i < searchFor.length(); i++) {
			char c = searchFor.charAt(searchFor.length() - 1 - i);
			if (!shiftTable.containsKey(c)) {
				shiftTable.put(c, i);
			}
		}
	}

	private int missMatch(char c) {
		return shiftTable.getOrDefault(c, searchFor.length());
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
						index += missMatch(searchIn.charAt(index));
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
