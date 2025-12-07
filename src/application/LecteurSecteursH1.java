package application;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LecteurSecteursH1 {


    private static Map<String, String> labels = new HashMap<>();


    private static Map<String, Double> quantitesParId = new HashMap<>();

    public static Graphe chargerSecteurs(String nomFichier) {
        Graphe g = new Graphe();
        labels.clear();
        quantitesParId.clear();
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

                String[] parts = ligne.split("\\s+", 2);
                String id = parts[0].trim();
                String label = (parts.length > 1 ? parts[1].trim() : id);

                labels.put(id, label);

                Noeud s = g.getNoeud(id);
                if (s == null) {
                    s = new Noeud(id);
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
                        if (j > i) {
                            Noeud a = noeuds.get(i);
                            Noeud b = noeuds.get(j);
                            String nomRue = "Voisinage" + compteurRue++;
                            g.ajouterArete(a, b, 1.0, nomRue, false);
                        }
                    }
                }
            }


            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                String[] parts = ligne.split("\\s+");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    double q = Double.parseDouble(parts[1]);
                    quantitesParId.put(id, q);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }


    public static String getLabel(String id) {
        String label = labels.get(id);
        return (label != null ? label : id);
    }


    public static Double getQuantite(String id) {
        return quantitesParId.get(id);
    }


    public static Map<String, Double> getQuantitesParId() {
        return Collections.unmodifiableMap(quantitesParId);
    }
}
