import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class TextProcessorTest {

	@Test
	public void test1() {
		SuffixTree t = new SuffixTree();
		t.addChar('a');
		t.addChar('b');

		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertEquals(3, t.getCount());
	}

	@Test
	public void test2() {
		SuffixTree t = new SuffixTree();
		t.addString("aabb");

		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertTrue(t.contains("aa"));
		assertTrue(t.contains("bb"));
		assertTrue(t.contains("aab"));
		assertTrue(t.contains("abb"));
		assertTrue(t.contains("aabb"));
		assertEquals(8, t.getCount());
	}

	@Test
	public void test3() {
		runRandomTest(2);
		runRandomTest(20);
		runRandomTest(60);
		runRandomTest(70);
		runRandomTest(100);
		runRandomTest(200);
	}

	@Test
	public void test12() {
		SuffixTree t = new SuffixTree();
		t.addString("xyzab");
		t.removeFirstNChar(3);

		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertEquals(3, t.getCount());
	}

	@Test
	public void test22() {
		SuffixTree t = new SuffixTree();
		t.addString("aaaabb");
		t.removeFirstNChar(2);
		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertTrue(t.contains("aa"));
		assertTrue(t.contains("bb"));
		assertTrue(t.contains("aab"));
		assertTrue(t.contains("abb"));
		assertTrue(t.contains("aabb"));
		assertEquals(8, t.getCount());
	}

	@Test
	public void test32() {
		runRandomTest(5);
		runRandomTest(20);
		runRandomTest(100);
		runRandomTest(700);
		runRandomTest(500);
		runRandomTest(600);
	}
	
	private void runRandomTest(int length) {
		String str = getRandomString(length);
		int i = (int) Math.random() * length;
		
		Set<String> substrs = getSet(str, i, str.length());
		SuffixTree tree = new SuffixTree();
		tree.addString(str);
		tree.removeFirstNChar(i);
		
		assertEquals(substrs.size(), tree.getCount());
		for (String s : substrs) {
			tree.contains(s);
		}
	}

	private String getRandomString(int length) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(r.nextInt(length));
		return sb.toString();
	}

	private Set<String> getSet(String str, int start, int end) {
		Set<String> set = new HashSet<>();
		for (int i = start; i < end; i++) {
			for (int j = i + 1; j <= end; j++) {
				set.add(str.substring(i, j));
			}
		}
		return set;
	}

}
