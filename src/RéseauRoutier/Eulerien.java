package RéseauRoutier;

import java.util.*;

public class Eulerien {
    private Graphe graphe;

    public Eulerien(Graphe g) {
        this.graphe = g;
    }

    /**
     * Calcule un circuit eulérien à partir d'un sommet de départ
     * Fonctionne si tous les sommets ont un degré pair et graphe non orienté
     */
    public List<Arete> calculerCircuit(Noeud depart) {
        // Copie locale des arêtes pour pouvoir les marquer comme utilisées
        Map<Noeud, LinkedList<Arete>> adjCopie = new HashMap<>();
        for (Map.Entry<Noeud, List<Arete>> entry : graphe.getAdjacence().entrySet()) {
            adjCopie.put(entry.getKey(), new LinkedList<>(entry.getValue()));
        }

        Stack<Noeud> pile = new Stack<>();
        List<Arete> circuit = new ArrayList<>();
        pile.push(depart);

        while (!pile.isEmpty()) {
            Noeud courant = pile.peek();
            LinkedList<Arete> voisins = adjCopie.get(courant);

            if (voisins != null && !voisins.isEmpty()) {
                // Prendre une arête non utilisée
                Arete ar = voisins.removeFirst();

                // Retirer l'arête inverse si graphe non orienté
                if (!ar.estOriente()) {
                    LinkedList<Arete> voisinsB = adjCopie.get(ar.getArrivee());
                    voisinsB.removeIf(a -> a.getArrivee().equals(courant) && a.getNomRue().equals(ar.getNomRue()));
                }

                pile.push(ar.getArrivee());
            } else {
                pile.pop();
                if (!pile.isEmpty()) {
                    Arete ar = graphe.getArete(pile.peek(), courant);
                    if (ar != null) circuit.add(ar);
                }
            }
        }

        Collections.reverse(circuit); // Pour obtenir le circuit dans le bon ordre
        return circuit;
    }
}