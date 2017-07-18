package grow;

import java.util.Collections;
import java.util.Map;
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

	// suffix tree representation
	private final Node root, preRoot;
	private Node activeNode;

	public SuffixTree() {
		this.str = new StringBuilder();
		this.preRoot = new Node(null, Integer.MIN_VALUE);
		this.preRoot.map = Collections.emptyMap();
		this.root = new Node(preRoot, Integer.MIN_VALUE);
		this.root.map = new TreeMap<>();
		this.root.setLink(preRoot);
		this.activeNode = root;
	}

	public int length() {
		return str.length();
	}

	public char charAt(int index) {
		return str.charAt(index);
	}

	public long getCount() {
		return this.root.count() - 1;
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
	}

	public void addString(String str) {
		for (int i = 0; i < str.length(); i++) {
			addChar(str.charAt(i));
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
		return String.format("str: %s%s", str, root);
	}

	private class Node {
		private Node parent, link;
		private int start;
		private Map<Character, Node> map;

		/**
		 * create a root
		 */
		public Node(Node parent, int start) {
			this.parent = parent;
			this.start = start;
		}

		public long count() {
			if (isLeaf()) {
				return length() - start;
			} else {
				long count = 1;
				for (Node child : map.values()) {
					count += child.count();
				}
				return count;
			}
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
			this.start = length() - 2;
			Node leaf = new Node(this, length() - 1);
			assert leaf.getStartChar() == toBeAdd;
			map.put(leaf.getStartChar(), leaf);
			return leaf;
		}

		public Node breakFirst() {
			Node oldLeaf = null;
			if (isLeaf()) {
				oldLeaf = new Node(this, start + 1);
				this.map = new TreeMap<>();
				this.map.put(oldLeaf.getStartChar(), oldLeaf);
			}
			this.start = length() - 1;
			return oldLeaf;
		}

		@Override
		public String toString() {
			return toString("");
		}

		public String toString(String prefix) {
			StringBuilder sb = new StringBuilder("\n" + prefix);
			if (isLeaf()) {
				for (int i = getStart(); i < length(); i++)
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
