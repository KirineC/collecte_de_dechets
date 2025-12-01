package algo;

import java.util.*;

public class MST {

    /**
     * Calcule un arbre couvrant de poids minimum avec l'algorithme de Prim.
     *
     * @param dist matrice des distances (graphe complet entre points de collecte)
     * @param root index du sommet de départ (le dépôt, ex: 0 si tspNodes[0] = "A")
     * @return tableau parent[i] = père de i dans l'arbre (root a parent[root] = -1)
     */
    public static int[] prim(double[][] dist, int root) {
        int n = dist.length;
        int[] parent = new int[n];
        double[] key = new double[n];
        boolean[] inMST = new boolean[n];

        Arrays.fill(parent, -1);
        Arrays.fill(key, Double.POSITIVE_INFINITY);

        key[root] = 0.0;

        for (int count = 0; count < n - 1; count++) {
            // 1) choisir le sommet u pas encore dans l'arbre avec la plus petite key[u]
            int u = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int v = 0; v < n; v++) {
                if (!inMST[v] && key[v] < best) {
                    best = key[v];
                    u = v;
                }
            }
            if (u == -1) {
                // graphe non connexe (en théorie ne devrait pas arriver si tout est joignable)
                break;
            }

            inMST[u] = true;

            // 2) mettre à jour les voisins de u
            for (int v = 0; v < n; v++) {
                double w = dist[u][v];
                if (!inMST[v] && w < key[v]) {
                    key[v] = w;
                    parent[v] = u;
                }
            }
        }

        return parent;
    }

    /**
     * Construit la liste d'adjacence de l'arbre à partir du tableau parent.
     */
    public static List<List<Integer>> buildTreeFromParent(int[] parent) {
        int n = parent.length;
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
        }
        for (int v = 0; v < n; v++) {
            int p = parent[v];
            if (p != -1) {
                tree.get(p).add(v);
                tree.get(v).add(p); // non orienté
            }
        }
        return tree;
    }

    /**
     * Parcours préfixe (DFS) de l'arbre MST pour obtenir un ordre de visite.
     *
     * @param tree arbre sous forme de liste d'adjacence
     * @param root racine du parcours (index du dépôt)
     * @return ordre de visite des indices (sans shortcutting)
     */
    public static List<Integer> dfsPreorder(List<List<Integer>> tree, int root) {
        int n = tree.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        dfsRec(tree, root, visited, order);
        return order;
    }

    private static void dfsRec(List<List<Integer>> tree, int u, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : tree.get(u)) {
            if (!visited[v]) {
                dfsRec(tree, v, visited, order);
            }
        }
    }
}
