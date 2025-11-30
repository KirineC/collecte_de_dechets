package application;

import RéseauRoutier.*;
import java.io.*;

public class LecteurTexte {

    public static Graphe chargerFichier(String nomFichier) {
        Graphe graphe = new Graphe();
        int compteurRue = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length < 5) continue; // pour H02/H03

                String idDepart = parts[0].trim();
                String idArrivee = parts[1].trim();
                double distance = Double.parseDouble(parts[2].trim());
                String orientation = parts[3].trim();
                int nbVoies = Integer.parseInt(parts[4].trim());

                String nomRue = "Rue" + compteurRue;
                compteurRue++;

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

                // Déterminer orientation de l'arête
                boolean estOriente;
                if (orientation.equalsIgnoreCase("simple")) {
                    estOriente = true;
                } else { // double sens
                    if (nbVoies >= 2) estOriente = true; // multi-voies, H02/H03
                    else estOriente = false;              // 1 voie, ramassage des 2 côtés
                }

                graphe.ajouterArete(depart, arrivee, distance, nomRue, estOriente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }
}