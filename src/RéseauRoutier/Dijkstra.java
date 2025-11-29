package RéseauRoutier;

import java.util.*;

public class Dijkstra {

    public static List<Noeud> plusCourtChemin(Graphe graphe, Noeud depart, Noeud arrivee) {

        Map<Noeud, Double> distance = new HashMap<>();
        Map<Noeud, Noeud> precedent = new HashMap<>();
        Set<Noeud> visited = new HashSet<>();
        PriorityQueue<Noeud> queue = new PriorityQueue<>(Comparator.comparing(distance::get));

        // Initialisation
        for (Noeud n : graphe.getNoeuds().values()) {
            distance.put(n, Double.MAX_VALUE);
            precedent.put(n, null);
        }
        distance.put(depart, 0.0);
        queue.add(depart);

        while (!queue.isEmpty()) {
            Noeud courant = queue.poll();
            if (!visited.add(courant)) continue; // ignore si déjà visité

            if (courant.equals(arrivee)) break;

            List<Arete> voisins = graphe.getAdjacence().getOrDefault(courant, new ArrayList<>());
            for (Arete a : voisins) {
                Noeud voisin = a.getArrivee();
                double nouvelleDist = distance.get(courant) + a.getDistance();

                if (nouvelleDist < distance.get(voisin)) {
                    distance.put(voisin, nouvelleDist);
                    precedent.put(voisin, courant);
                    queue.add(voisin); // on ajoute simplement, la PriorityQueue sera triée au prochain poll
                }
            }
        }

        // Reconstruction du chemin
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