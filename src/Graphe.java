import java.util.*;

public class Graphe {
    private Map<String, Noeud> noeuds = new HashMap<>();
    private Map<Noeud, List<Arete>> adjacence = new HashMap<>();

    public void ajouterNoeud(Noeud n) {
        noeuds.put(n.getId(), n);
        adjacence.putIfAbsent(n, new ArrayList<>());
    }

    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue) {
        Arete ab = new Arete(a, b, distance, nomRue);
        adjacence.get(a).add(ab);

        // Graphe non orienté dans Thème 1 → ajouter l'arête inverse
        Arete ba = new Arete(b, a, distance, nomRue);
        adjacence.get(b).add(ba);
    }

    public Noeud getNoeud(String id) {
        return noeuds.get(id);
    }

    public Map<String, Noeud> getNoeuds() { return noeuds; }
    public Map<Noeud, List<Arete>> getAdjacence() { return adjacence; }
}
