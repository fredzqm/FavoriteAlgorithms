import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class SuffixTreeTest {

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
		runRandomTest(500);
		runRandomTest(1000);
		runRandomTest(2000);
	}

	@Test
	public void test4() {
		SuffixTree t = new SuffixTree();
		t.addString("xyzab");
		t.removeFirstChar();
		t.removeFirstChar();
		t.removeFirstChar();

		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertEquals(3, t.getCount());
	}

	@Test
	public void test5() {
		SuffixTree t = new SuffixTree();
		t.addString("aaaabb");
		t.removeFirstChar();
		t.removeFirstChar();
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
	public void test6() {
		runRandomWindowTest(2);
		runRandomWindowTest(20);
		runRandomWindowTest(60);
		runRandomWindowTest(70);
		runRandomWindowTest(100);
		runRandomWindowTest(200);
		runRandomWindowTest(500);
	}

	@Test
	public void test7() {
		SuffixTree t = new SuffixTree();
		t.addString("750");
		t.removeFirstChar();
		t.addChar('0');
		assertEquals(5, t.getCount());
	}

	@Test
	public void test8() {
		SuffixTree t = new SuffixTree();
		t.addString("3717950");
		t.removeFirstChar();
		t.addChar('1');
		t.removeFirstChar();
		t.addChar('3');
		t.removeFirstChar();
		t.addChar('1');
		assertEquals(27, t.getCount());
	}

	@Test
	public void test9() {
		SuffixTree t = new SuffixTree();
		t.addString("cdddcd");
		t.removeFirstChar();
		assertEquals(11, t.getCount());
	}
	
	private void runRandomTest(int length) {
		String str = getRandomString(length);
		int start = (int) Math.random() * length;

		Set<String> substrs = getSet(str, start, str.length());
		SuffixTree tree = new SuffixTree();
		tree.addString(str);
		tree.removeFirstNChar(start);

		assertEquals(substrs.size(), tree.getCount());
		for (String s : substrs) {
			tree.contains(s);
		}
	}

	private void runRandomWindowTest(int length) {
		String str = getRandomString(length);
		int start = (int) Math.random() * length;

		Set<String> substrs = getSet(str, start, str.length());
		int W = length - start;
		SuffixTree tree = simulate(length, str, W);

		if (substrs.size() != tree.getCount()) {
			System.out.printf("str: %s\n  start: %d\n  window: %d\n  substr: %s", str, start, W, str.substring(start));
			simulate(length, str, W);
			fail();
		}
		for (String s : substrs) {
			tree.contains(s);
		}
	}

	private SuffixTree simulate(int length, String str, int W) {
		SuffixTree tree = new SuffixTree();
		for (int i = 0; i < length; i++) {
			if (i >= W)
				tree.removeFirstChar();
			tree.addChar(str.charAt(i));
		}
		return tree;
	}

	private String getRandomString(int length) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append((char) ('a' + r.nextInt(26)));
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
