package stringSearch;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class BoyerMooreTest {

	@Test
	public void test() {
		BoyerMooreMatcher matcher = new BoyerMooreMatcher("AB");
		
		Iterator<Integer> x = matcher.findMatchIn("ABCBAB");
		assertTrue(x.hasNext());
		assertEquals(0 , (int) x.next());
		assertTrue(x.hasNext());
		assertEquals(4 , (int) x.next());
		assertFalse(x.hasNext());
	}
	
	
	@Test
	public void test2() {
		BoyerMooreMatcher matcher = new BoyerMooreMatcher("aabbc");
		
		Iterator<Integer> x = matcher.findMatchIn("aadaabbcaabc");
		assertTrue(x.hasNext());
		assertEquals(3 , (int) x.next());
		assertFalse(x.hasNext());
	}

}
