package application;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;
import algo.WelshPowell;

import java.util.*;

public class MainTheme3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== THEME 3 : Planification des collectes ===");
        System.out.println("Choisissez une hypothèse :");
        System.out.println("1 - Hypothèse 1 : Coloration sans contrainte de capacité");
        System.out.println("2 - Hypothèse 2 : Coloration avec contrainte de capacité");
        System.out.print("Votre choix : ");
        int choix = sc.nextInt();

        String fichier = "RueilSecteursH1.txt";
        Graphe g = LecteurSecteursH1.chargerSecteurs(fichier);

        System.out.println("\nNombre de secteurs : " + g.getNoeuds().size());

        Map<Noeud, Integer> coloration;

        if (choix == 1) {

            System.out.println("\n=== Hypothèse 1 : Coloration simple ===");
            coloration = WelshPowell.colorier(g);

        } else if (choix == 2) {
            System.out.println("\n=== Hypothèse 2 : Coloration avec capacité ===");


            Map<Noeud, Double> quantites = new HashMap<>();
            for (Noeud n : g.getNoeuds().values()) {
                String id = n.getId();
                Double q = LecteurSecteursH1.getQuantite(id);
                if (q == null) q = 0.0;
                quantites.put(n, q);
            }


            System.out.print("Capacité d’un camion C : ");
            double C = sc.nextDouble();

            System.out.print("Nombre de camions disponibles N : ");
            int N = sc.nextInt();


            double capaciteJour = N * C;
            for (Noeud s : g.getNoeuds().values()) {
                double qs = quantites.getOrDefault(s, 0.0);
                if (qs > capaciteJour) {
                    System.out.println(" Attention : le secteur " + LecteurSecteursH1.getLabel(s.getId())
                            + " (" + s.getId() + ") dépasse la capacité journalière !");
                    System.out.println("   q_s = " + qs + " > N*C = " + capaciteJour);
                    System.out.println("   → Ce secteur ne pourra pas être collecté en une seule journée.");
                }
            }
            // ===========================================

            coloration = WelshPowell.colorierAvecCapacite(g, quantites, C, N);
        }
        else {
            System.out.println("Choix invalide.");
            return;
        }

        // Regroupement par jour
        Map<Integer, List<String>> parJour = new TreeMap<>();
        for (Map.Entry<Noeud, Integer> e : coloration.entrySet()) {
            int jour = e.getValue();
            String id = e.getKey().getId();
            String label = LecteurSecteursH1.getLabel(id);
            parJour.computeIfAbsent(jour, k -> new ArrayList<>()).add(label);
        }

        // Affichage du planning
        System.out.println("\nPlanning proposé :");
        for (Map.Entry<Integer, List<String>> e : parJour.entrySet()) {
            int jour = e.getKey();
            List<String> secteurs = e.getValue();
            Collections.sort(secteurs);
            System.out.println("Jour " + jour + " : " + String.join(", ", secteurs));
        }

        System.out.println("\nNombre total de jours utilisés : " + parJour.size());
    }
}
