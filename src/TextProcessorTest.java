import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class TextProcessorTest {

	@Test
	public void test() {
		runRandomTest(4, 3, 6);
		runRandomTest(10, 7, 5);
		runRandomTest(10, 7, 5);
		runRandomTest(10, 7, 5);
		runRandomTest(10, 7, 5);
		runRandomTest(40, 9, 5);
		runRandomTest(50, 10, 10);
		runRandomTest(100, 20, 20);
		runRandomTest(1000, 50, 2);
		runRandomTest(10000, 50, 20);
	}

	@Test
	public void test3() {
		runTest("770", 3, new int[] { 1 });
		runTest("abc", 3, new int[] { 1 });
		runTest("123", 3, new int[] { 1 });
		runTest("780", 3, new int[] { 1 });
	}

	@Test
	public void test4() {
		runTest("0000", 3, new int[] { 1, 2 });
		runTest("2201220", 7, new int[] { 1 });
		runTest("2001001", 7, new int[] { 1 });
		runTest("1110222", 7, new int[] { 1 });
	}

	@Test
	public void test5() {
		runTest("0012012", 7, new int[] { 1 });
		runTest("0000120120", 7, new int[] { 1, 3 });
	}

	@Test
	public void test6() {
		runTest("7500", 3, new int[] { 1 });
		runTest("3717950131", 7, new int[] { 1, 3 });
	}
	
	private void runRandomTest(int length, int W, int Q) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(r.nextInt(10));

		int[] ques = new int[Q];
		for (int i = 0; i < Q; i++) {
			ques[i] = r.nextInt(length - W) + 1;
		}
		runTest(sb.toString(), W, ques);
	}

	private void runTest(String str, int W, int[] ques) {
		StringBuilder sb = new StringBuilder();
		sb.append(ques[0]);
		for (int i = 1; i < ques.length; i++)
			sb.append(", " + ques[i]);
		System.out.printf("String: \"%s\"\nW: %d\nques: new int[]{%s}\n\n", str, W, sb);

		TextProcessor textProcessor = new TextProcessor(str, W);
		long[] found = textProcessor.solve(ques);
		for (int qi = 0; qi < ques.length; qi++) {
			int start = ques[qi] - 1;
			int end = start + W;
			start = Math.max(start, 0);
			end = Math.min(end, str.length());

			Set<String> set = new HashSet<>();
			for (int i = start; i < end; i++) {
				for (int j = i + 1; j <= end; j++) {
					set.add(str.substring(i, j));
				}
			}
			printSet(set);
			assertEquals("from: " + start + " to: " + end + " string: " + str.substring(start, end), set.size(),
					found[qi]);
		}
	}

	private void printSet(Set<String> set) {
		ArrayList<String> ls = new ArrayList<>(set);
		Collections.sort(ls);
		System.out.println(ls);
	}

}
