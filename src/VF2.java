import java.util.*;

public class VF2 {
    private int[][] g1, g2; // матриці суміжності графів
    private boolean[] used1, used2; // списки використаних вершин
    private int[] core1, core2; // відображення між вершинами
    private int n1, n2, m1, m2; // кількість вершин і дуг у графах

    public VF2(int[][] g1, int[][] g2) {
        this.g1 = g1;
        this.g2 = g2;
        n1 = g1.length;
        n2 = g2.length;
        m1 = 0;
        m2 = 0;
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n1; j++) {
                if (g1[i][j] > 0) m1++;
            }
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n2; j++) {
                if (g2[i][j] > 0) m2++;
            }
        }
        used1 = new boolean[n1];
        used2 = new boolean[n2];
        core1 = new int[n1];
        core2 = new int[n2];
        Arrays.fill(core1, -1);
        Arrays.fill(core2, -1);
    }

    public void run() {
        search(0);
    }

    private void search(int depth) {
        if (depth == n1) {
            printSolution();
            return;
        }

        int[] candidates = getCandidates(depth);
        for (int i : candidates) {
            if (match(depth, i)) {
                search(depth + 1);
                unmatch(depth, i);
            }
        }
    }

    private int[] getCandidates(int depth) {
        List<Integer> list = new ArrayList<>();
        if (depth == 0) {
            for (int i = 0; i < n2; i++) {
                list.add(i);
            }
        } else {
            for (int i = 0; i < n2; i++) {
                if (used2[i]) continue;
                if (hasConsistentEdges(depth, i)) {
                    list.add(i);
                }
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    private boolean hasConsistentEdges(int u, int v) {
        for (int i = 0; i < n1; i++) {
            if (g1[u][i] > 0 && core1[i] != -1 && g2[v][core2[core1[i]]] == 0) {
                return false;
            }
        }
        for (int i = 0; i < n1; i++) {
            if (g1[i][u] > 0 && core1[i] != -1 && g2[core2[core1[i]]][v] == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean match(int u, int v) {
        if (!hasConsistentEdges(u, v)) {
            return false;
        }

        used1[u] = true;
        used2[v] = true;
        core1[u] = v;
        core2[v] = u;

        for (int i = 0; i < n1; i++) {
            if (g1[u][i] > 0 && core1[i] == -1) {
                int j = findCandidate(i);
                if (j == -1) {
                    unmatch(u, v);
                    return false;
                }
                if (!match(i, j)) {
                    unmatch(u, v);
                    return false;
                }
            }
        }

        return true;
    }

    private int findCandidate(int u) {
        for (int i = 0; i < n2; i++) {
            if (used2[i]) continue;
            if (hasConsistentEdges(u, i)) {
                return i;
            }
        }
        return -1;
    }

    private void unmatch(int u, int v) {
        core1[u] = -1;
        core2[v] = -1;
        used1[u] = false;
        used2[v] = false;
    }

    private void printSolution() {
        System.out.print("Core1: ");
        for (int i = 0; i < n1; i++) {
            System.out.print(core1[i] + " ");
        }
        System.out.print("Core2: ");
        for (int i = 0; i < n2; i++) {
            System.out.print(core2[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[][] g1 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        int[][] g2 = {{0, 1, 1}, {1, 0, 0}, {1, 0, 0}};
        VF2 vf2 = new VF2(g1, g2);
        vf2.run();
    }
}
