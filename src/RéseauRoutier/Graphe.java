package RéseauRoutier;

import java.util.*;

public class Graphe {
    private Map<String, Noeud> noeuds = new HashMap<>();
    private Map<Noeud, List<Arete>> adjacence = new HashMap<>();

    // Ajouter un nœud
    public void ajouterNoeud(Noeud n) {
        noeuds.put(n.getId(), n);
        adjacence.putIfAbsent(n, new ArrayList<>());
    }

    // Ajouter une arête
    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue) {
        // Arête a → b
        adjacence.get(a).add(new Arete(a, b, distance, nomRue));

        // Graphe non orienté dans HO1
        adjacence.get(b).add(new Arete(b, a, distance, nomRue));
    }

    // Récupérer un noeud
    public Noeud getNoeud(String id) {
        return noeuds.get(id);
    }

    // Récupérer l'arête reliant a → b
    public Arete getArete(Noeud a, Noeud b) {
        List<Arete> liste = adjacence.get(a);
        if (liste == null) return null;

        for (Arete ar : liste) {
            if (ar.getArrivee().equals(b)) {
                return ar;
            }
        }
        return null;
    }

    // Récupérer le poids (distance) d'une arête
    public double getPoids(Noeud a, Noeud b) {
        Arete ar = getArete(a, b);
        return (ar != null ? ar.getDistance() : Double.POSITIVE_INFINITY);
    }

    // Récupérer tous les noeuds
    public Map<String, Noeud> getNoeuds() {
        return noeuds;
    }

    // Récupérer la liste d'adjacence
    public Map<Noeud, List<Arete>> getAdjacence() {
        return adjacence;
    }
}