package maxFlow;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MaxFlow {
	static class Edge {
		Vertex from, to;
		int capacity;
		int flow;

		public Edge(Vertex from, Vertex to, int capacity) {
			this.from = from;
			this.to = to;
			this.capacity = capacity;
		}
	}

	static class Vertex {
		List<Edge> edge = new ArrayList<>();
		int maxFlowFromSink = 0;
		Edge prevEdge;
		int itr;

		public void addEdgeTo(Vertex to, int capacity) {
			Edge edge = new Edge(this, to, capacity);
			this.edge.add(edge);
			to.edge.add(edge);
		}

		public int capacity(Edge e) {
			if (e.from == this) {
				return e.capacity - e.flow;
			} else {
				return e.flow;
			}
		}

		public Vertex connectTo(Edge e) {
			if (e.from == this)
				return e.to;
			return e.from;
		}
	}

	private static int maxFlow(Vertex source, Vertex sink) {
		int itr = 1;
		int maxFlow = 0;
		while (true) {
			// bfs find argument path
			Queue<Vertex> queue = new LinkedList<>();
			queue.offer(source);
			source.maxFlowFromSink = Integer.MAX_VALUE;
			source.prevEdge = null;
			source.itr = itr;
			bfs: while (!queue.isEmpty()) {
				Vertex poped = queue.poll();
				for (Edge e : poped.edge) {
					Vertex n = poped.connectTo(e);
					if (n.itr != itr) {
						int maxFlowToN = Math.min(poped.maxFlowFromSink, poped.capacity(e));
						if (maxFlowToN > 0) {
							n.maxFlowFromSink = maxFlowToN;
							n.prevEdge = e;
							n.itr = itr;
							if (n == sink)
								break bfs;
							queue.offer(n);
						}
					}
				}
			}
			if (sink.itr != itr)
				break;
			itr++;
			// apply argument path
			int flow = sink.maxFlowFromSink;
			Vertex argumentPath = sink;
			Edge prevEdge;
			while ((prevEdge = argumentPath.prevEdge) != null) {
				if (prevEdge.to == argumentPath) {
					prevEdge.flow += flow;
					argumentPath = prevEdge.from;
				} else {
					prevEdge.flow -= flow;
					argumentPath = prevEdge.to;
				}
			}
			maxFlow += flow;
		}
		return maxFlow;
	}

	public static void main(String[] args) {
		Vertex source = new Vertex();
		Vertex sink = new Vertex();
		Vertex a = new Vertex();
		Vertex b = new Vertex();
		Vertex c = new Vertex();
		Vertex d = new Vertex();
		source.addEdgeTo(a, 20);
		source.addEdgeTo(b, 5);
		a.addEdgeTo(b, 3);
		c.addEdgeTo(sink, 9);
		b.addEdgeTo(c, 4);
		b.addEdgeTo(sink, 2);
		sink.addEdgeTo(d, Integer.MAX_VALUE);
		System.out.println(maxFlow(source, sink));
	}
}
