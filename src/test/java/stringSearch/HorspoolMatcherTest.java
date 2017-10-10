package stringSearch;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class HorspoolMatcherTest {

	@Test
	public void test() {
		HorspoolMatcher matcher = new HorspoolMatcher("aabbc");
		
		Iterator<Integer> x = matcher.findMatchIn("aadaabbcaabc");
		assertTrue(x.hasNext());
		assertEquals(3 , (int) x.next());
		assertFalse(x.hasNext());
	}

}
