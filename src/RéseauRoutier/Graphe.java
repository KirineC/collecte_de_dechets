package RéseauRoutier;

import java.util.*;

public class Graphe {
    private Map<String, Noeud> noeuds = new HashMap<>();
    private Map<Noeud, List<Arete>> adjacence = new HashMap<>();

    public void ajouterNoeud(Noeud n) {
        noeuds.put(n.getId(), n);
        adjacence.putIfAbsent(n, new ArrayList<>());
    }

    // H02/H03 : estOriente = true si sens unique ou double sens multi-voies
    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue, boolean estOriente) {
        adjacence.get(a).add(new Arete(a, b, distance, nomRue, estOriente));
        if (!estOriente) {
            adjacence.get(b).add(new Arete(b, a, distance, nomRue, estOriente));
        }
    }

    public Noeud getNoeud(String id) { return noeuds.get(id); }
    public Map<Noeud, List<Arete>> getAdjacence() { return adjacence; }
    public Map<String, Noeud> getNoeuds() { return noeuds; }

    public Arete getArete(Noeud a, Noeud b) {
        for (Arete ar : adjacence.getOrDefault(a, new ArrayList<>())) {
            if (ar.getArrivee().equals(b)) return ar;
        }
        return null;
    }
}