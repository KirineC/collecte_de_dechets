package application;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LecteurSecteursH1 {

    // Map id -> nom complet du quartier (S1 -> "Belle rive")
    private static Map<String, String> labels = new HashMap<>();

    public static Graphe chargerSecteurs(String nomFichier) {
        Graphe g = new Graphe();
        labels.clear();  // au cas où on recharge un autre fichier
        int compteurRue = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne = br.readLine();
            if (ligne == null) {
                System.out.println("Fichier vide : " + nomFichier);
                return g;
            }

            int n = Integer.parseInt(ligne.trim()); // nombre de sommets

            // 1) Lecture des identifiants de secteurs + labels
            ArrayList<Noeud> noeuds = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                ligne = br.readLine();
                if (ligne == null) break;
                ligne = ligne.trim();
                if (ligne.isEmpty()) {
                    i--;
                    continue;
                }

                // On prend le premier mot comme ID, le reste comme label
                String[] parts = ligne.split("\\s+", 2);
                String id = parts[0].trim();
                String label = (parts.length > 1 ? parts[1].trim() : id);

                labels.put(id, label); // mémorise le nom complet

                Noeud s = g.getNoeud(id);
                if (s == null) {
                    s = new Noeud(id); // Noeud ne connaît que l'id
                    g.ajouterNoeud(s);
                }
                noeuds.add(s);
            }

            // 2) Lecture de la matrice d'adjacence n x n
            for (int i = 0; i < n; i++) {
                ligne = br.readLine();
                if (ligne == null) break;
                ligne = ligne.trim();
                if (ligne.isEmpty()) {
                    i--;
                    continue;
                }

                String[] vals = ligne.split("\\s+");
                if (vals.length < n) {
                    System.out.println("Ligne matrice trop courte à la ligne " + (i + 1 + n) + " !");
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if ("1".equals(vals[j])) {
                        // Matrice symétrique : on ne crée l'arête que pour j > i
                        if (j > i) {
                            Noeud a = noeuds.get(i);
                            Noeud b = noeuds.get(j);
                            String nomRue = "Voisinage" + compteurRue++;
                            g.ajouterArete(a, b, 1.0, nomRue, false);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }

    // Récupérer le nom complet à partir de l'id
    public static String getLabel(String id) {
        String label = labels.get(id);
        return (label != null ? label : id);
    }
}
