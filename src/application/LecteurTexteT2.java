package application;

import RéseauRoutier.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecteurTexteT2 {

    /**
     * Lit un fichier texte de type "Vendome.txt" :
     * - Lignes de capacité :   P2;CAP;4
     * - Lignes d'arêtes :      A;B;12.5
     */
    public static Graphe chargerFichier(String path) throws IOException {
        Graphe g = new Graphe();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // ignorer les lignes vides ou commentaires
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String from = parts[0].trim();
                String to   = parts[1].trim();
                String wStr = parts[2].trim();

                // S'assurer que le nœud 'from' existe
                Noeud fromNode = g.getNoeud(from);
                if (fromNode == null) {
                    fromNode = new Noeud(from);
                    g.ajouterNoeud(fromNode);
                }

                // 🔹 Cas 1 : ligne de capacité →  P1;CAP;4
                if (to.equalsIgnoreCase("CAP")) {
                    int cap = Integer.parseInt(wStr);
                    g.setCapacity(fromNode, cap);
                }
                // 🔹 Cas 2 : ligne d'arête →  A;B;12.5
                else {
                    double distance = Double.parseDouble(wStr);

                    // S'assurer que le nœud 'to' existe
                    Noeud toNode = g.getNoeud(to);
                    if (toNode == null) {
                        toNode = new Noeud(to);
                        g.ajouterNoeud(toNode);
                    }

                    // Graphe NON ORIENTÉ → deux sens
                    String nomRue = from + "-" + to; // ou autre format
                    g.ajouterArete(fromNode, toNode, distance, nomRue, false);
                }
            }
        }

        return g;
    }
}
