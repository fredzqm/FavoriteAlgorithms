package simplex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simplex {
	static class Equation {
		Map<String, Double> coefficient = new HashMap<>();
		double equalTo;
		String name;

		public Equation() {
			this.name = toString();
		}

		public Equation(String name) {
			this.name = name;
		}

		public Equation add(String var, double coef) {
			coefficient.put(var, coef);
			return this;
		}

		public Equation eqaulTo(double value) {
			equalTo = value;
			return this;
		}

		public Equation lessThanOrEqual(double var) {
			return add(name, 1).eqaulTo(var);
		}
	}

	/**
	 * m variable n constraints m > n
	 * 
	 * @param constraints
	 *            n*(m+1) matrix
	 * @param optimize
	 *            m+1 array
	 * @return the value for each valuable and the final optimal value
	 */
	public static Map<String, Double> simplexMaximize(List<Equation> constraints, Equation optimize) {
		int index = 0;
		Map<String, Integer> varIndex = new HashMap<>();
		for (Equation eq : constraints) {
			for (String var : eq.coefficient.keySet()) {
				if (!varIndex.containsKey(var)) {
					varIndex.put(var, index++);
				}
			}
		}
		int numVar = varIndex.size();
		double[][] simplexMatrix = new double[constraints.size() + 1][numVar + 1];
		int i = 0;
		for (Equation eq : constraints) {
			for (Map.Entry<String, Double> coef : eq.coefficient.entrySet()) {
				simplexMatrix[i][varIndex.get(coef.getKey())] = coef.getValue();
			}
			simplexMatrix[i][numVar] = eq.equalTo;
			i++;
		}
		for (Map.Entry<String, Double> coef : optimize.coefficient.entrySet()) {
			simplexMatrix[i][varIndex.get(coef.getKey())] = -coef.getValue();
		}
		if (!simplexOptimize(simplexMatrix))
			return null;
		Map<String, Double> result = new HashMap<>();
		nextVar: for (Map.Entry<String, Integer> var : varIndex.entrySet()) {
			int j = var.getValue();
			if (simplexMatrix[i][j] == 0) {
				for (int a = 0; a < i; a++) {
					if (simplexMatrix[a][j] != 0) {
						elementarize(simplexMatrix, a, j);
						result.put(var.getKey(), simplexMatrix[a][numVar]);
						continue nextVar;
					}
				}
			}
		}
		result.put("", simplexMatrix[i][numVar]);
		return result;
	}

	/**
	 * 
	 * @param simplexMatrix
	 * @return true if solved, false if unbound.
	 */
	public static boolean simplexOptimize(double[][] simplexMatrix) {
		int n = simplexMatrix.length - 1;
		int m = simplexMatrix[0].length - 1;
		while (true) {
			int col = -1;
			double colMin = 0;
			for (int a = 0; a < m; a++) {
				if (simplexMatrix[n][a] < colMin) {
					colMin = simplexMatrix[n][a];
					col = a;
				}
			}
			if (col == -1)
				return true;
			int row = -1;
			double minRatio = Double.MAX_VALUE;
			for (int b = 0; b < n; b++) {
				if (simplexMatrix[b][col] > 0) {
					double ratio = simplexMatrix[b][m] / simplexMatrix[b][col];
					if (ratio < minRatio) {
						minRatio = ratio;
						row = b;
					}
				}
			}
			if (row == -1)
				return false;
			elementarize(simplexMatrix, row, col);
		}
	}

	public static void elementarize(double[][] matrix, int row, int col) {
		assert matrix[row][col] != 0;
		if (matrix[row][col] != 1) {
			double div = matrix[row][col];
			for (int a = 0; a < matrix[0].length; a++) {
				matrix[row][a] = matrix[row][a] / div;
			}
		}
		for (int b = 0; b < matrix.length; b++) {
			if (b != row && matrix[b][col] != 0) {
				double mult = matrix[b][col];
				for (int a = 0; a < matrix[b].length; a++) {
					matrix[b][a] = matrix[b][a] - matrix[row][a] * mult;
				}
			}
		}
	}

	public static void main(String[] args) {

		double[][] m = new double[][] { { 2, 1, 6, 1, 0, 0, 14 }, { 4, 2, 3, 0, 1, 0, 28 }, { 2, 5, 5, 0, 0, 1, 30 } };
		System.out.println(Arrays.deepToString(m));
		elementarize(m, 0, 0);
		System.out.println(Arrays.deepToString(m));

		double[][] result = new double[][] { { 2, 1, 1, 1, 0, 0, 14 }, { 4, 2, 3, 0, 1, 0, 28 },
				{ 2, 5, 5, 0, 0, 1, 30 }, { -1, -2, 1, 0, 0, 0, 0 } };
		simplexOptimize(result);
		System.out.println(Arrays.deepToString(result));

		System.out.println(simplexMaximize(
				Arrays.asList(new Equation("Eq1").add("a", 2).add("b", 1).add("c", 1).lessThanOrEqual(14),
						new Equation("Eq2").add("a", 4).add("b", 2).add("c", 3).lessThanOrEqual(28),
						new Equation("Eq3").add("a", 2).add("b", 5).add("c", 5).lessThanOrEqual(30)),
				new Equation().add("a", 1).add("b", 2).add("c", -1)));
		// {=13.0, a=5.0, b=4.0, Eq2=0.0}

		System.out.println(simplexMaximize(
				Arrays.asList(new Equation("Eq1").add("a", 2).add("b", 3).add("c", 2).lessThanOrEqual(1000),
						new Equation("Eq2").add("a", 1).add("b", 1).add("c", 2).lessThanOrEqual(800)),
				new Equation().add("a", 7).add("b", 8).add("c", 10)));
		// {=105.0, a=15.0, Eq1=970.0, Eq2=785.0}

	}

}
