package RéseauRoutier;

import java.util.*;

public class Dijkstra {

    public static List<Noeud> plusCourtChemin(Graphe graphe, Noeud depart, Noeud arrivee) {
        Map<Noeud, Double> distance = new HashMap<>();
        Map<Noeud, Noeud> precedent = new HashMap<>();
        Set<Noeud> visited = new HashSet<>();
        PriorityQueue<Noeud> queue = new PriorityQueue<>(Comparator.comparing(distance::get));

        for (Noeud n : graphe.getNoeuds().values()) {
            distance.put(n, Double.MAX_VALUE);
            precedent.put(n, null);
        }

        distance.put(depart, 0.0);
        queue.add(depart);

        while (!queue.isEmpty()) {
            Noeud courant = queue.poll();
            if (!visited.add(courant)) continue;
            if (courant.equals(arrivee)) break;

            for (Arete ar : graphe.getAdjacence().getOrDefault(courant, new ArrayList<>())) {
                Noeud voisin = ar.getArrivee();
                double nouvDist = distance.get(courant) + ar.getDistance();
                if (nouvDist < distance.get(voisin)) {
                    distance.put(voisin, nouvDist);
                    precedent.put(voisin, courant);
                    queue.add(voisin);
                }
            }
        }

        List<Noeud> chemin = new ArrayList<>();
        Noeud actuel = arrivee;
        while (actuel != null) {
            chemin.add(actuel);
            actuel = precedent.get(actuel);
        }
        Collections.reverse(chemin);
        return chemin;
    }
}