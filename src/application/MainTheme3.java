package application;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;
import algo.WelshPowell;

import java.util.*;

public class MainTheme3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== THEME 3 - Hypothèse 1 : Coloration des secteurs ===");

        String fichier = "RueilSecteursH1.txt";  // adapte le nom si besoin
        Graphe g = LecteurSecteursH1.chargerSecteurs(fichier);

        System.out.println("Nombre de secteurs : " + g.getNoeuds().size());

        Map<Noeud, Integer> coloration = WelshPowell.colorier(g);

        // Regrouper par couleur (jour)
        Map<Integer, List<String>> parJour = new TreeMap<>();
        for (Map.Entry<Noeud, Integer> e : coloration.entrySet()) {
            int couleur = e.getValue();
            String id = e.getKey().getId();

            // 🔹 On récupère le nom complet du quartier à partir de l'ID
            String label = LecteurSecteursH1.getLabel(id);

            parJour.computeIfAbsent(couleur, k -> new ArrayList<>()).add(label);
        }

        // Affichage
        System.out.println("\nPlanning proposé (Hypothèse 1) :");
        for (Map.Entry<Integer, List<String>> e : parJour.entrySet()) {
            int jour = e.getKey();
            List<String> secteurs = e.getValue();
            Collections.sort(secteurs);
            System.out.println("Jour " + jour + " : " + String.join(", ", secteurs));
        }

        System.out.println("\nNombre total de jours utilisés : " + parJour.size());
    }
}
