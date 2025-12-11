    package RéseauRoutier;

    import java.util.*;

    public class Dijkstra {

        public static List<Noeud> plusCourtChemin(Graphe graphe, Noeud depart, Noeud arrivee) {
            Map<Noeud, Double> distance = new HashMap<>();//map distance pour chaque noeud
            Map<Noeud, Noeud> precedent = new HashMap<>();//map noeud precedents pour reconstruire le chemin
            Set<Noeud> visited = new HashSet<>();
            PriorityQueue<Noeud> queue = new PriorityQueue<>(Comparator.comparing(distance::get));//traiter noeud les plus proches

            for (Noeud n : graphe.getNoeuds().values()) {
                distance.put(n, Double.MAX_VALUE);
                precedent.put(n, null);
            }

            distance.put(depart, 0.0);
            queue.add(depart);

            while (!queue.isEmpty()) {
                Noeud courant = queue.poll();//retire noeud avec plus petite distance
                if (!visited.add(courant)) continue;
                if (courant.equals(arrivee)) break;

                for (Arete ar : graphe.getAdjacence().getOrDefault(courant, new ArrayList<>())) {
                    Noeud voisin = ar.getArrivee();
                    double nouvDist = distance.get(courant) + ar.getDistance();//calcule distance
                    if (nouvDist < distance.get(voisin)) {//si dist plus petite -> ajoute voisin a la queue
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
                actuel = precedent.get(actuel);//reconstruit chemin
            }
            Collections.reverse(chemin);
            return chemin;
        }





        public static class Result {
            public final Map<Noeud, Double> dist;  // distance minimale depuis la source
            public final Map<Noeud, Noeud> prev;   // prédécesseur sur le plus court chemin

            public Result(Map<Noeud, Double> dist, Map<Noeud, Noeud> prev) {
                this.dist = dist;
                this.prev = prev;
            }


            public List<Noeud> buildPath(Noeud source, Noeud target) {
                List<Noeud> path = new ArrayList<>();
                Noeud curr = target;

                Double d = dist.get(target);
                if (d == null || d == Double.POSITIVE_INFINITY) {
                    // pas de chemin connu
                    return path;
                }

                while (curr != null) {
                    path.add(0, curr);
                    if (curr.equals(source)) break;
                    curr = prev.get(curr);
                }
                return path;
            }
        }


        public static Result shortestPaths(Graphe g, Noeud source) {
            Map<Noeud, Double> dist = new HashMap<>();
            Map<Noeud, Noeud> prev = new HashMap<>();

            // Initialisation
            for (Noeud node : g.getNoeuds().values()) {
                dist.put(node, Double.POSITIVE_INFINITY);
                prev.put(node, null);
            }
            dist.put(source, 0.0);

            PriorityQueue<Map.Entry<Noeud, Double>> pq =
                    new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
            pq.add(new AbstractMap.SimpleEntry<>(source, 0.0));

            Set<Noeud> visited = new HashSet<>();

            // Dijkstra
            while (!pq.isEmpty()) {
                Noeud u = pq.poll().getKey();
                if (!visited.add(u)) continue;  // déjà traité

                for (Arete e : g.getAdjacence().getOrDefault(u, Collections.emptyList())) {
                    Noeud v = e.getArrivee();
                    double newDist = dist.get(u) + e.getDistance();

                    if (newDist < dist.get(v)) {
                        dist.put(v, newDist);
                        prev.put(v, u);
                        pq.add(new AbstractMap.SimpleEntry<>(v, newDist));
                    }
                }
            }

            return new Result(dist, prev);
        }

    }