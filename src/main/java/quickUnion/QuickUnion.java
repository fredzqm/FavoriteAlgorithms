package quickUnion;

import java.util.Arrays;

public class QuickUnion {
    private int[] parent;

    public QuickUnion(int size) {
        this.parent = new int[size];
        Arrays.fill(this.parent, -1);
    }

    public int find(int x) {
        if (this.parent[x] == -1) {
            return x;
        }
        int set = find(this.parent[x]);
        this.parent[x] = set;
        return set;
    }

    public void union(int a, int b) {
        int seta = find(a);
        int setb = find(b);
        if (seta != setb)
            this.parent[seta] = setb;
    }
}
