import static org.junit.Assert.*;

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

}
