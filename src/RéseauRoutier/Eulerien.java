package RéseauRoutier;

import java.util.*;

public class Eulerien {
    private Graphe graphe;

    public Eulerien(Graphe g) {
        this.graphe = g;
    }

    // ---- Trouver les sommets impairs ----
    public List<Noeud> sommetsDegreImpair() {
        List<Noeud> impairs = new ArrayList<>();
        for (Noeud n : graphe.getNoeuds().values()) {
            int degre = graphe.getAdjacence().get(n).size();
            if (degre % 2 != 0) {
                impairs.add(n);
            }
        }
        return impairs;
    }

    // ---- Dupliquer les arêtes d’un chemin ----
    private void dupliquerChemin(List<Noeud> chemin) {
        for (int i = 0; i < chemin.size() - 1; i++) {
            Noeud a = chemin.get(i);
            Noeud b = chemin.get(i+1);
            Arete ar = graphe.getArete(a, b);

            if (ar != null) {
                // AJOUT CORRIGÉ : ajouter une copie de l'arête existante
                graphe.ajouterArete(a, b, ar.getDistance(), ar.getNomRue(), ar.estOriente());
            }
        }
    }

    // ---- Hierholzer (inchangé) ----
    public List<Arete> calculerCircuit(Noeud depart) {

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
                Arete ar = voisins.removeFirst();

                if (!ar.estOriente()) {
                    LinkedList<Arete> voisinsB = adjCopie.get(ar.getArrivee());
                    voisinsB.removeIf(a -> a.getArrivee().equals(courant)
                            && a.getNomRue().equals(ar.getNomRue()));
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

        Collections.reverse(circuit);
        return circuit;
    }

    // ---- Méthode générale 0 ou 2 impairs ----
    public List<Arete> calculerCircuitGeneral(Noeud depot) {
        List<Noeud> impairs = sommetsDegreImpair();

        if (impairs.size() == 0) {
            return calculerCircuit(depot); // Circuit eulérien
        }

        if (impairs.size() == 2) {
            Noeud a = impairs.get(0);
            Noeud b = impairs.get(1);

            List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, a, b);
            dupliquerChemin(chemin);

            return calculerCircuit(depot);
        }

        System.out.println("Cas non géré : plus de 2 sommets impairs.");
        return new ArrayList<>();
    }
}