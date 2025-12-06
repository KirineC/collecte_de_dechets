package application;

import RéseauRoutier.Graphe;
import RéseauRoutier.Noeud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecteurTexteT2 {

    /**
     * Charger un fichier de graphe pour le Thème 2.
     * Format attendu :
     *   - Routes :  A;B;distance
     *   - Capacités : Pi;CAP;valeur
     *
     * ho = 1 -> HO1 : non orienté (double sens)
     * ho = 2 -> HO2 : orienté
     * ho = 3 -> HO3 : mixte (orienté, double sens codé par deux lignes A;B et B;A)
     */
    public static Graphe chargerFichier(String nomFichier, int ho) {
        Graphe g = new Graphe();

        // HO1 = non orienté (double sens)
        // HO2 / HO3 = on respecte le sens écrit dans le fichier
        boolean estOrienteGlobal = (ho != 1);

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;        // ignore les lignes vides
                if (ligne.startsWith("#")) continue;  // ignore les commentaires

                String[] parts = ligne.split(";");
                if (parts.length != 3) {
                    // Ligne mal formée, on ignore
                    continue;
                }

                String id1 = parts[0].trim();
                String id2 = parts[1].trim();
                String val = parts[2].trim();

                // ====== CASE CAPACITÉS : P1;CAP;6 ======
                if (id2.equalsIgnoreCase("CAP")) {
                    int cap;
                    try {
                        cap = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // valeur non valide -> on ignore
                        continue;
                    }

                    Noeud p = g.getNoeud(id1);
                    if (p == null) {
                        p = new Noeud(id1);
                        g.ajouterNoeud(p);
                    }
                    g.setCapacity(p, cap);
                }

                // ====== CASE ROUTES : A;B;distance ======
                else {
                    double distance;
                    try {
                        distance = Double.parseDouble(val);
                    } catch (NumberFormatException e) {
                        continue; // distance invalide -> on ignore
                    }

                    Noeud depart = g.getNoeud(id1);
                    if (depart == null) {
                        depart = new Noeud(id1);
                        g.ajouterNoeud(depart);
                    }

                    Noeud arrivee = g.getNoeud(id2);
                    if (arrivee == null) {
                        arrivee = new Noeud(id2);
                        g.ajouterNoeud(arrivee);
                    }

                    String nomRue = id1 + "-" + id2;

                    // HO1 : estOrienteGlobal = false -> ajoute aussi l'arête inverse
                    // HO2 / HO3 : estOrienteGlobal = true -> on respecte le sens du fichier
                    g.ajouterArete(depart, arrivee, distance, nomRue, estOrienteGlobal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }

    /**
     * Surcharge pratique :
     * si tu appelles LecteurTexteT2.chargerFichier("fichier"),
     * on considère HO1 (non orienté) par défaut.
     */
    public static Graphe chargerFichier(String nomFichier) {
        return chargerFichier(nomFichier, 1);
    }
}
