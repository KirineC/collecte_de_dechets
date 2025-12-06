package application;

import RéseauRoutier.*;
import java.io.*;

public class LecteurTexteT2 {

    public static Graphe chargerFichier(String nomFichier, int ho) {
        Graphe graphe = new Graphe();
        int compteurRue = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;

                String[] parts = ligne.split(";");
                // Format à 3 champs : A;B;distance  OU  Pi;CAP;valeur
                if (parts.length != 3) continue;

                String idDepart = parts[0].trim();
                String idArrivee = parts[1].trim();
                String val3 = parts[2].trim();

                // Cas capacité : Pi;CAP;X
                if (idArrivee.equalsIgnoreCase("CAP")) {
                    int cap = Integer.parseInt(val3);
                    Noeud n = graphe.getNoeud(idDepart);
                    if (n == null) {
                        n = new Noeud(idDepart);
                        graphe.ajouterNoeud(n);
                    }
                    graphe.setCapacity(n, cap);
                    continue;
                }

                // Cas arête : A;B;distance
                double distance = Double.parseDouble(val3);

                Noeud depart = graphe.getNoeud(idDepart);
                if (depart == null) {
                    depart = new Noeud(idDepart);
                    graphe.ajouterNoeud(depart);
                }

                Noeud arrivee = graphe.getNoeud(idArrivee);
                if (arrivee == null) {
                    arrivee = new Noeud(idArrivee);
                    graphe.ajouterNoeud(arrivee);
                }

                boolean estOriente;
                if (ho == 1) {
                    // HO1 : on considère le graphe comme non orienté (double sens)
                    estOriente = false;
                } else {
                    // HO2 / HO3 : on respecte l’orientation donnée dans le fichier
                    estOriente = true;
                }

                String nomRue = "Rue" + compteurRue++;
                graphe.ajouterArete(depart, arrivee, distance, nomRue, estOriente);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }
}
