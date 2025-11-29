import java.util.*;

public class Dijkstra {

    public static List<Noeud> plusCourtChemin(Graphe graphe, Noeud depart, Noeud arrivee) {

        Map<Noeud, Double> distance = new HashMap<>();
        Map<Noeud, Noeud> precedent = new HashMap<>();
        PriorityQueue<Noeud> file = new PriorityQueue<>(Comparator.comparing(distance::get));

        for (Noeud n : graphe.getNoeuds().values()) {
            distance.put(n, Double.MAX_VALUE);
            precedent.put(n, null);
        }

        distance.put(depart, 0.0);
        file.add(depart);

        while (!file.isEmpty()) {
            Noeud courant = file.poll();

            if (courant.equals(arrivee)) break;

            for (Arete a : graphe.getAdjacence().get(courant)) {
                Noeud voisin = a.getArrivee();
                double nouvelleDist = distance.get(courant) + a.getDistance();

                if (nouvelleDist < distance.get(voisin)) {
                    distance.put(voisin, nouvelleDist);
                    precedent.put(voisin, courant);
                    file.add(voisin);
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
