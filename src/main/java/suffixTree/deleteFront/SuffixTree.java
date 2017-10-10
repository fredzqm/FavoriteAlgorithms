package suffixTree.deleteFront;
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
	private Queue<Leaf> leaves = new LinkedList<>();

	public SuffixTree() {
		this.str = new StringBuilder();
		this.offset = 0;
		this.first = 0;
		this.preRoot = new Node(null, -2, Integer.MIN_VALUE);
		this.preRoot.map = Collections.emptyMap();
		this.root = new Node(preRoot, -1, Integer.MIN_VALUE);
		this.root.map = new TreeMap<>();
		this.root.setLink(preRoot);
		this.activeNode = root;
		this.count = 0;
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return str.length() - offset - 1;
	}

	public int length() {
		return str.length() - offset - first;
	}

	public char charAt(int index) {
		return str.charAt(index + offset);
	}

	public long getCount() {
		return count;
	}

	public void addChar(char c) {
		str.append(c);
		Node match = activeNode.getNextNode(c);
		if (match == null) {
			Node lastcreatedLeaf = activeNode.createLeaf(c);
			while (true) {
				activeNode = activeNode.getLink();
				match = activeNode.getNextNode(c);
				if (match != null) {
					lastcreatedLeaf.setLink(match);
					break;
				}
				Node createdLeaf = activeNode.createLeaf(c);
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
		Node removed = pollLeaf();
		first++;
		assert removed.isLeaf();
		int removeCount = getLast() - removed.getStart();
		int startFrom = removed.getStartFrom();
		do {
			removeCount++;
			Node parent = removed.parent;
			parent.map.remove(removed.getStartChar());
			if (parent.map.isEmpty())
				parent.map = null;
			removed = parent;
		} while (startFrom == removed.getStartFrom());
		count -= removeCount;
		while (activeNode.isLeaf()) {
			pushLeaf(activeNode);
			activeNode = activeNode.getLink();
		}
	}

	protected Node pollLeaf() {
		return leaves.poll().get();
	}

	protected void pushLeaf(Node leafNode) {
		Leaf l = new Leaf(leafNode);
		leafNode.leaf = l;
		leaves.add(l);
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
		return String.format("str: %s%s", str.substring(offset + getFirst(), offset + getLast() + 1), root);
	}

	private class Node {
		private Node parent, link;
		private int start, level;
		private Map<Character, Node> map;
		private Leaf leaf;

		/**
		 * create a root
		 */
		public Node(Node parent, int level, int start) {
			this.parent = parent;
			this.level = level;
			this.start = start;
		}

		public Node getLink() {
			return link;
		}

		public void setLink(Node link) {
			this.link = link;
		}

		public int getStartFrom() {
			return start - level;
		}

		public int getStart() {
			return start;
		}

		public boolean isLeaf() {
//			assert (map == null) == (leaf != null);
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
			Node leaf = new Node(this, level + 1, getLast());
			assert leaf.getStartChar() == toBeAdd;
			map.put(leaf.getStartChar(), leaf);
			pushLeaf(leaf);
			return leaf;
		}

		public Node breakFirst() {
			Node oldLeaf = null;
			if (isLeaf()) {
				oldLeaf = new Node(this, level + 1, start + 1);
				this.map = new TreeMap<>();
				this.map.put(oldLeaf.getStartChar(), oldLeaf);
				leaf.set(oldLeaf);
			}
			this.start = getLast();
			return oldLeaf;
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

	public class Leaf {
		private Node leafNode;

		public Leaf(Node leafNode) {
			this.leafNode = leafNode;
		}

		public Node get() {
			return leafNode;
		}

		public void set(Node newLeafNode) {
			this.leafNode.leaf = null;
			newLeafNode.leaf = this;
			this.leafNode = newLeafNode;
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
				if (index <= getLast() && c == charAt(index + 1)) {
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
