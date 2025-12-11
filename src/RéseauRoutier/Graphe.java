package RéseauRoutier;

import java.util.*;

public class Graphe {

    // Liste des nœuds du graphe indexés/ID
    private Map<String, Noeud> noeuds = new HashMap<>();

    // Liste d’adjacence
    private Map<Noeud, List<Arete>> adjacence = new HashMap<>();

    private Map<Noeud, Integer> capacites = new HashMap<>();
    // Ajouter un nœud au graphe
    public void ajouterNoeud(Noeud n) {

        // Ajoute le nœud dans la map d’identification
        noeuds.put(n.getId(), n);

        // Initialise sa liste d’adjacence s'il n’existe pas encore
        adjacence.putIfAbsent(n, new ArrayList<>());
    }

    // Ajouter une arête orientée ou non entre deux nœuds
    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue, boolean estOriente) {

        // Ajout de l’arête allant de A vers B
        adjacence.get(a).add(new Arete(a, b, distance, nomRue, estOriente));

        // Si la rue n’est ps orientée → on ajoute automatiquement l’arête retour B → A
        if (!estOriente) {
            adjacence.get(b).add(new Arete(b, a, distance, nomRue, estOriente));
        }
    }



    /*
    public void ajouterArete(Noeud a, Noeud b, double distance, String nomRue, int sens) {
        // sens = 1 → rue à sens unique ou double multivoies
        // sens = 0 → double sens à une voie

        if (sens == 1) {
            // Ajout A->B uniquement
            adjacence.get(a).add(new Arete(a, b, distance, nomRue, true));

            // Si rue double multivoies : possibilité d'ajouter B->A
            // adjacence.get(b).add(new Arete(b, a, distance, nomRue, true));

        } else {
            // Rue à double sens à une voie
            adjacence.get(a).add(new Arete(a, b, distance, nomRue, false));
            adjacence.get(b).add(new Arete(b, a, distance, nomRue, false));
        }
    }
    */


    // Récupération d’un nœud à partir de son ID
    public Noeud getNoeud(String id) {
        return noeuds.get(id);
    }


    // Récupérer l’arête entre deux nœuds
    public Arete getArete(Noeud a, Noeud b) {

        // Récupère la liste des arêtes sortant de A
        List<Arete> liste = adjacence.get(a);

        // Si pas d’arêtes sortantes → rien à chercher
        if (liste != null) {

            // On cherche une arête dont l’arrivée correspond à B
            for (Arete ar : liste) {
                if (ar.getArrivee().equals(b)) return ar;
            }
        }

        // Aucun lien trouvé
        return null;
    }



    public Map<String, Noeud> getNoeuds() { return noeuds; }

    public Map<Noeud, List<Arete>> getAdjacence() { return adjacence; }



    public void setCapacity(Noeud n, int cap) {
        capacites.put(n, cap);
    }

    public int getCapacity(Noeud n) {
        return capacites.getOrDefault(n, 0);
    }

    public int getCapacity(String id) {
        Noeud n = noeuds.get(id);
        if (n == null) return 0;
        return capacites.getOrDefault(n, 0);
    }
}
