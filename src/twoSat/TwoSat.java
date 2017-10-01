package twoSat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwoSat {
	static class Variable {
		Vertex a = new Vertex(), b = new Vertex();

		public Vertex get(boolean invert) {
			if (invert)
				return b;
			return a;
		}

		public boolean getValue() {
			return a.connectedGraphId > b.connectedGraphId;
		}

		public boolean notConsistent() {
			return this.a.connectedGraphId == this.b.connectedGraphId;
		}
	}

	static class Vertex {
		Collection<Vertex> implies = new HashSet<>();
		Integer connectedGraphId;

		public void addImplies(Vertex vertex) {
			this.implies.add(vertex);
		}

		public void postOrderTranversal(Set<Vertex> reached, List<Vertex> postOrder) {
			if (reached.contains(this))
				return;
			reached.add(this);
			for (Vertex v : implies) {
				v.postOrderTranversal(reached, postOrder);
			}
			postOrder.add(this);
		}

		public void setConnectedGraphRoot(int connectedId) {
			if (connectedGraphId != null)
				return;
			connectedGraphId = connectedId;
			for (Vertex v : implies) {
				v.setConnectedGraphRoot(connectedId);
			}
		}
	}

	static void addOrConstraint(Variable x, boolean xInvert, Variable y, boolean yInvert) {
		x.get(!xInvert).addImplies(y.get(yInvert));
		y.get(!yInvert).addImplies(x.get(xInvert));
	}

	public static boolean twoSatSolve(Collection<Variable> variableList) {
		List<Vertex> postOrder = new ArrayList<>();
		Set<Vertex> reached = new HashSet<>();
		for (Variable v : variableList) {
			v.a.postOrderTranversal(reached, postOrder);
			v.b.postOrderTranversal(reached, postOrder);
		}
		int index = 0;
		for (Vertex v : postOrder) {
			if (v.connectedGraphId == null) {
				v.setConnectedGraphRoot(++index);
			}
		}
		for (Variable v : variableList) {
			if (v.notConsistent())
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Variable x = new Variable();
		Variable y = new Variable();
		Variable z = new Variable();
		addOrConstraint(x, true, y, false);
		addOrConstraint(x, false, x, false);
		addOrConstraint(y, true, y, true);
		
		System.out.println(twoSatSolve(Arrays.asList(x, y, z)));
		System.out.println(x.getValue());
		System.out.println(y.getValue());
		System.out.println(z.getValue());
	}

}
