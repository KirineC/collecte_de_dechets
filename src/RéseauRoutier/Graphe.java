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

    // Ajouter une arête avec orientation
    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue, boolean estOriente) {
        adjacence.get(a).add(new Arete(a, b, distance, nomRue, estOriente));
        if (!estOriente) {
            // si double sens, ajouter automatiquement l’arête inverse
            adjacence.get(b).add(new Arete(b, a, distance, nomRue, estOriente));
        }
    }

    public Noeud getNoeud(String id) {
        return noeuds.get(id);
    }

    public Arete getArete(Noeud a, Noeud b) {
        List<Arete> liste = adjacence.get(a);
        if (liste != null) {
            for (Arete ar : liste) {
                if (ar.getArrivee().equals(b)) return ar;
            }
        }
        return null;
    }

    public Map<String, Noeud> getNoeuds() { return noeuds; }
    public Map<Noeud, List<Arete>> getAdjacence() { return adjacence; }
}