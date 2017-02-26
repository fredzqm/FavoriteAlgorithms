import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * https://open.kattis.com/problems/textprocessor
 * 
 * https://gist.github.com/makagonov/22ab3675e3fc0031314e8535ffcbee2c
 * http://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * 
 * @author zhang
 *
 */
public class SuffixTree {
	// basic string representation
	private final StringBuilder str;
	private int offset, first;

	// suffix tree representation
	final Node root, preRoot;
	private Node activeNode;
	private long count;
	private Queue<Node> leaves = new LinkedList<>();

	public SuffixTree() {
		this.str = new StringBuilder();
		this.offset = 0;
		this.first = 0;
		this.preRoot = new Node(null, Integer.MAX_VALUE);
		this.preRoot.map = Collections.emptyMap();
		this.root = new Node(preRoot, Integer.MAX_VALUE);
		this.root.map = new TreeMap<>();
		this.root.setLink(preRoot);
		this.activeNode = root;
		this.count = 0;
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return first + str.length() - offset - 1;
	}

	public int length() {
		return str.length() - offset;
	}

	public long getCount() {
		return count;
	}

	public char charAt(int index) {
		return str.charAt(index - first + offset);
	}

	public void addChar(char c) {
		str.append(c);
		Node match = activeNode.getNextNode(c);
		if (match == null) {
			Node lastcreatedLeaf = activeNode.createLeaf(c);
			leaves.add(lastcreatedLeaf);
			while (true) {
				activeNode = activeNode.getLink();
				match = activeNode.getNextNode(c);
				if (match != null) {
					lastcreatedLeaf.setLink(match);
					break;
				}
				Node createdLeaf = activeNode.createLeaf(c);
				leaves.add(createdLeaf);
				lastcreatedLeaf.setLink(createdLeaf);
				lastcreatedLeaf = createdLeaf;
			}
		}
		activeNode = match;
		Node lastcreatedLeaf = match.breakFirst();
		if (lastcreatedLeaf != null) {
			// a new leaf is created when creating interior node
			c = lastcreatedLeaf.getStartChar();
			while (true) {
				match = match.getLink();
				if (!match.isLeaf()) {
					Node link = match.getNextNode(c);
					assert link != null;
					lastcreatedLeaf.setLink(link);
					break;
				}
				Node link = match.breakFirst();
				assert link.getStartChar() == c;
				lastcreatedLeaf.setLink(link);
				lastcreatedLeaf = link;
			}
		}
		count += leaves.size();
	}

	public void removeFirstChar() {
		Node removed = leaves.poll();
		count -= removed.remove();
	}

	public void addString(String str) {
		for (int i = 0; i < str.length(); i++) {
			addChar(str.charAt(i));
		}
	}

	public void removeFirstNChar(int n) {
		for (int i = 0; i < n; i++) {
			removeFirstChar();
		}
	}

	public boolean contains(String substr) {
		TreeTraverser itr = traverser();
		for (int i = 0; i < substr.length(); i++) {
			char c = substr.charAt(i);
			if (!itr.accept(c))
				return false;
		}
		return true;
	}

	public TreeTraverser traverser() {
		return new TreeTraverser(root);
	}

	@Override
	public String toString() {
		return str.substring(offset + getFirst());
	}

	private class Node {
		private Node parent, link, longest;
		private int start;
		private Map<Character, Node> map;

		/**
		 * create a root
		 */
		public Node(Node parent, int start) {
			this.parent = parent;
			this.start = start;
		}

		public Node getLink() {
			return link;
		}

		public void setLink(Node link) {
			this.link = link;
		}

		public int getStart() {
			return start;
		}

		public boolean isLeaf() {
			return map == null;
		}

		public Node getNextNode(char c) {
			if (this == preRoot)
				return root;
			return map.get(c);
		}

		public char getStartChar() {
			return str.charAt(start);
		}

		public Node createLeaf(char toBeAdd) {
			this.start = getLast() - 1;
			Node leaf = new Node(this, getLast());
			assert leaf.getStartChar() == toBeAdd;
			map.put(leaf.getStartChar(), leaf);
			return leaf;
		}

		public Node breakFirst() {
			Node oldLeaf = null;
			if (isLeaf()) {
				oldLeaf = new Node(this, start + 1);
				this.map = new TreeMap<>(); // convert this to Interior node
				this.map.put(oldLeaf.getStartChar(), oldLeaf);
				this.longest = oldLeaf;
			}
			this.start = getLast();
			return oldLeaf;
		}

		public long remove() {
			if (isLeaf()) {
				int len = getLast() - getStart();
				Node p = this, c;
				do {
					len++;
					c = p;
					p = c.parent;
					p.map.remove(c.getStartChar());
				} while (p.map.size() == 0 && p.getStart() + 1 == c.getStart());
				return len;
			} else {
				Node l = this;
				while (!l.longest.isLeaf())
					l = l.longest;
				l.map.remove(l.longest.getStartChar());
				return getLast() - l.longest.getStart() + 1;
			}
		}

		@Override
		public String toString() {
			return toString("");
		}

		public String toString(String prefix) {
			StringBuilder sb = new StringBuilder("\n" + prefix);
			if (isLeaf()) {
				for (int i = getStart(); i <= getLast(); i++)
					sb.append(charAt(i));
			} else {
				if (this != root) {
					sb.append(getStartChar());
					prefix += ' ';
				}
				for (Node n : map.values()) {
					sb.append(prefix + n.toString(prefix));
				}
			}
			return sb.toString();
		}
	}

	public class TreeTraverser {
		private Node curNode;
		private int index;

		public TreeTraverser(Node node) {
			this.curNode = node;
		}

		public boolean accept(char c) {
			if (curNode.isLeaf()) {
				if (index < length() && c == charAt(index + 1)) {
					index++;
					return true;
				} else {
					return false;
				}
			} else {
				Node next = curNode.getNextNode(c);
				if (next == null) {
					return false;
				} else {
					curNode = next;
					index = curNode.getStart();
					assert charAt(index) == c;
					return true;
				}
			}
		}

		public void undoLast() {
			if (curNode.isLeaf()) {
				if (index == curNode.start) {
					curNode = curNode.parent;
				} else {
					index--;
				}
			} else {
				if (curNode == root)
					throw new RuntimeException("Cannot undoLastChar, because it is already an empty string");
				curNode = curNode.parent;
			}
		}

	}

}
