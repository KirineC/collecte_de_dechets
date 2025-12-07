package algo;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;
import RéseauRoutier.Arete;

import java.util.*;

public class WelshPowell {


    public static Map<Noeud, Integer> colorier(Graphe g) {

        // 1) Liste des sommets
        List<Noeud> sommets = new ArrayList<>(g.getNoeuds().values());

        // 2) Tri par degré décroissant
        sommets.sort((a, b) -> Integer.compare(degre(g, b), degre(g, a)));

        Map<Noeud, Integer> couleur = new HashMap<>();
        int couleurCourante = 0;

        // 3) Parcours des sommets dans cet ordre
        for (Noeud s : sommets) {
            if (couleur.containsKey(s)) continue; // déjà coloré

            couleurCourante++;
            couleur.put(s, couleurCourante);

            // on essaie d'ajouter un maximum d'autres sommets à cette couleur
            for (Noeud t : sommets) {
                if (couleur.containsKey(t)) continue; // déjà coloré

                if (peutPrendreCouleur(g, t, couleur, couleurCourante)) {
                    couleur.put(t, couleurCourante);
                }
            }
        }

        return couleur;
    }

    public static Map<Noeud, Integer> colorierAvecCapacite(Graphe g,
                                                           Map<Noeud, Double> quantites,
                                                           double C,
                                                           int N) {

        // 1) Liste des sommets
        List<Noeud> sommets = new ArrayList<>(g.getNoeuds().values());

        sommets.sort((a, b) -> Integer.compare(degre(g, b), degre(g, a)));

        Map<Noeud, Integer> couleur = new HashMap<>();
        Map<Integer, Double> chargeJour = new HashMap<>();

        int nbJours = 0;
        double capaciteJour = N * C;

        // 3) Affectation des secteurs un par un
        for (Noeud s : sommets) {
            double qs = quantites.getOrDefault(s, 0.0);

            int jourChoisi = -1;

            // on essaie de le placer dans un jour existant
            for (int jour = 1; jour <= nbJours; jour++) {
                double chargeActuelle = chargeJour.getOrDefault(jour, 0.0);

                if (peutPrendreCouleur(g, s, couleur, jour)
                        && chargeActuelle + qs <= capaciteJour) {
                    jourChoisi = jour;
                    break;
                }
            }

            // si aucun jour existant ne convient, on crée un nouveau jour
            if (jourChoisi == -1) {
                nbJours++;
                jourChoisi = nbJours;
                chargeJour.put(jourChoisi, 0.0);
            }

            couleur.put(s, jourChoisi);
            chargeJour.put(jourChoisi, chargeJour.get(jourChoisi) + qs);
        }

        return couleur;
    }

    /** Degré = nombre de voisins (sortants) dans le graphe */
    private static int degre(Graphe g, Noeud n) {
        return g.getAdjacence().getOrDefault(n, Collections.emptyList()).size();
    }

    /** Vérifie si t peut prendre la couleur c sans conflit avec ses voisins déjà colorés */
    private static boolean peutPrendreCouleur(Graphe g,
                                              Noeud t,
                                              Map<Noeud, Integer> couleur,
                                              int c) {
        for (Arete e : g.getAdjacence().getOrDefault(t, Collections.emptyList())) {
            Noeud voisin = e.getArrivee();
            Integer cv = couleur.get(voisin);
            if (cv != null && cv == c) {
                return false;
            }
        }
        return true;
    }
}
