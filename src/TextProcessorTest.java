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
		runRandomTest(500);
	}

	@Test
	public void test4() {
		SuffixTree t = new SuffixTree();
		t.addString("xyzab");
		t.removeFirstNChar(3);

		assertTrue(t.contains("a"));
		assertTrue(t.contains("b"));
		assertTrue(t.contains("ab"));
		assertEquals(3, t.getCount());
	}

	@Test
	public void test5() {
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
	public void test6() {
		runRandomWindowTest(2);
		runRandomWindowTest(20);
		runRandomWindowTest(60);
		runRandomWindowTest(70);
		runRandomWindowTest(100);
		runRandomWindowTest(200);
		runRandomWindowTest(500);
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
		SuffixTree tree = new SuffixTree();
		for (int i = 0; i < length; i++) {
			if (i >= W)
				tree.removeFirstChar();
			tree.addChar(str.charAt(i));
		}
		
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
