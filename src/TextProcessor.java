import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

/**
 * https://open.kattis.com/problems/textprocessor
 * 
 * https://gist.github.com/makagonov/22ab3675e3fc0031314e8535ffcbee2c
 * http://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * 
 * @author zhang
 *
 */
public class TextProcessor {
	static Scanner in = new Scanner(System.in);
	private final String str;
	private final int W;

	public TextProcessor(String str, int W) {
		this.str = str;
		this.W = W;
	}

	public long[] solve(int[] ques) {
		int Q = ques.length;
		ArrayList<Answer> answers = new ArrayList<>();
		for (int i = 0; i < Q; i++)
			answers.add(new Answer(ques[i] + W - 2, i));

		Collections.sort(answers, new Comparator<Answer>() {
			@Override
			public int compare(Answer o1, Answer o2) {
				return o1.end - o2.end;
			}
		});

		Iterator<Answer> itr = answers.iterator();
		Answer nextToSolve = itr.next();

		// initialize
		SuffixTree tree = new SuffixTree();
		for (int i = 0; i < str.length(); i++) {
			// remove
			if (i >= W) {
				tree.removeFirstChar();
			}
			tree.addChar(str.charAt(i));
			if (i == nextToSolve.end) {
				nextToSolve.value = tree.getCount();
				while (itr.hasNext()) {
					nextToSolve = itr.next();
					if (i == nextToSolve.end)
						nextToSolve.value = tree.getCount();
					else
						break;
				}
			}
		}

		Collections.sort(answers, new Comparator<Answer>() {
			@Override
			public int compare(Answer o1, Answer o2) {
				return o1.index - o2.index;
			}
		});

		long[] ans = new long[Q];
		for (int i = 0; i < Q; i++)
			ans[i] = answers.get(i).value;
		return ans;
	}

	public static class Answer {
		int end;
		int index;
		long value;

		public Answer(int end, int i) {
			this.end = end;
			this.index = i;
		}

	}

	public static void main(String[] args) {
		in.hasNext();
		String str = in.next();
		int Q = in.nextInt();
		int W = in.nextInt();
		int[] ques = new int[Q];
		for (int i = 0; i < Q; i++) {
			ques[i] = in.nextInt();
		}
		TextProcessor textProcessor = new TextProcessor(str, W);

		long[] x = textProcessor.solve(ques);

		for (long l : x)
			System.out.println(l);
	}

}
