package RéseauRoutier;

import java.util.*;

public class Graphe {
    private Map<String, Noeud> noeuds = new HashMap<>();
    private Map<Noeud, List<Arete>> adjacence = new HashMap<>();

    private Map<Noeud, Integer> capacites = new HashMap<>();

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


    // POUR H03
    //public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue, int sens) {
        // sens = 1 → rue à sens unique ou double multivoies, 0 → double à une voie
      //  if (sens == 1) {
            // Rue simple ou double multivoies → ajout d'une arête unique (A->B)
        //    adjacence.get(a).add(new Arete(a, b, distance, nomRue, true));
          //  if (!"simple".equals(nomRue)) {
                // Si double multivoies, ajouter également l'arête inverse (B->A) pour représenter le second passage
         //   //    adjacence.get(b).add(new Arete(b, a, distance, nomRue, true));
           // }
      //  } else {
            // Rue double sens à une voie → arêtes A->B et B->A
        //    adjacence.get(a).add(new Arete(a, b, distance, nomRue, false));
      //      adjacence.get(b).add(new Arete(b, a, distance, nomRue, false));
    //    }
  //  }

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

    /** Attribue une capacité à un nœud (ex: P2 = 5 unités de déchets) */
    public void setCapacity(Noeud n, int cap) {
        capacites.put(n, cap);
    }

    /** Get à partir du Noeud */
    public int getCapacity(Noeud n) {
        return capacites.getOrDefault(n, 0);
    }

    /** Get à partir du String (id) */
    public int getCapacity(String id) {
        Noeud n = noeuds.get(id);
        if (n == null) return 0;
        return capacites.getOrDefault(n, 0);
    }

}
