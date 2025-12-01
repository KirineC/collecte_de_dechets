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

    private void dupliquerChemin(List<Noeud> chemin) {
        for (int i = 0; i < chemin.size() - 1; i++) {
            Noeud a = chemin.get(i);
            Noeud b = chemin.get(i + 1);
            Arete arExistante = graphe.getArete(a, b);

            if (arExistante != null) {
                graphe.ajouterArete(a, b, arExistante.getDistance(),
                        arExistante.getNomRue() + "_dup",
                        arExistante.estOriente());
            }
        }
    }

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
                    Noeud precedent = pile.peek();

                    // Recherche de l'arête utilisée pour relier "precedent" -> "courant"
                    Arete arUtilisee = null;
                    for (Arete arAdj : graphe.getAdjacence().get(precedent)) {
                        if ((arAdj.getDepart().equals(precedent) && arAdj.getArrivee().equals(courant)) ||
                                (!arAdj.estOriente() && arAdj.getDepart().equals(courant) && arAdj.getArrivee().equals(precedent))) {
                            arUtilisee = arAdj;
                            break;
                        }
                    }

                    if (arUtilisee != null) circuit.add(arUtilisee);
                }
            }
        }

        Collections.reverse(circuit);
        return circuit;
    }

    public List<Arete> calculerCircuitGeneral(Noeud depot) {
        List<Noeud> impairs = sommetsDegreImpair();

        if (impairs.isEmpty()) return calculerCircuit(depot); // Cas idéal : tous pairs

        if (impairs.size() % 2 != 0) {
            System.out.println("Nombre impair de sommets impairs : impossible de créer un circuit eulérien complet.");
            return new ArrayList<>();
        }

        // --- Cas 2 sommets impairs ---
        if (impairs.size() == 2) {
            List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, impairs.get(0), impairs.get(1));
            dupliquerChemin(chemin);
            return calculerCircuit(depot);
        }

        // --- Cas général : heuristique "plus proche voisin" ---
        Set<Noeud> nonCouples = new HashSet<>(impairs);

        while (!nonCouples.isEmpty()) {
            Noeud n1 = nonCouples.iterator().next();
            nonCouples.remove(n1);

            Noeud plusProche = null;
            double distMin = Double.MAX_VALUE;

            for (Noeud n2 : nonCouples) {
                List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, n1, n2);
                double dist = 0;
                for (int i = 1; i < chemin.size(); i++) {
                    Arete ar = graphe.getArete(chemin.get(i-1), chemin.get(i));
                    dist += (ar != null ? ar.getDistance() : 0);
                }
                if (dist < distMin) {
                    distMin = dist;
                    plusProche = n2;
                }
            }

            if (plusProche != null) {
                List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, n1, plusProche);
                dupliquerChemin(chemin);
                nonCouples.remove(plusProche);
            }
        }

        return calculerCircuit(depot);
    }
}