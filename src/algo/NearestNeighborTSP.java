package algo;

import java.util.*;

public class NearestNeighborTSP {

    public static List<String> tour(List<String> points, String start, double[][] dist) {
        List<String> tour = new ArrayList<>();
        Set<String> nonVisited = new HashSet<>(points);


        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            index.put(points.get(i), i);
        }

        String current = start;
        tour.add(current);
        nonVisited.remove(current);

        while (!nonVisited.isEmpty()) {
            String best = null;
            double bestDist = Double.POSITIVE_INFINITY;
            int iCurrent = index.get(current);

            for (String p : nonVisited) {
                int j = index.get(p);
                double d = dist[iCurrent][j];
                if (d < bestDist) {
                    bestDist = d;
                    best = p;
                }
            }

            tour.add(best);
            nonVisited.remove(best);
            current = best;
        }

        // retour au dépôt
        if (!current.equals(start)) {
            tour.add(start);
        }

        return tour;
    }

    public static double tourLength(List<String> tour, List<String> points, double[][] dist) {
        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            index.put(points.get(i), i);
        }

        double total = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int a = index.get(tour.get(i));
            int b = index.get(tour.get(i + 1));
            total += dist[a][b];
        }
        return total;
    }
}
