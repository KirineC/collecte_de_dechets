package application;

import RéseauRoutier.*;
import java.io.*;

public class LecteurTexte {

    public static Graphe chargerFichier(String nomFichier) {
        Graphe graphe = new Graphe();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                // Format : idDepart;latDepart;lonDepart;idArrivee;latArrivee;lonArrivee;nomRue;distance
                String[] parts = ligne.split(";");
                if (parts.length != 8) continue;

                String idDepart = parts[0];
                double latDepart = Double.parseDouble(parts[1]);
                double lonDepart = Double.parseDouble(parts[2]);

                String idArrivee = parts[3];
                double latArrivee = Double.parseDouble(parts[4]);
                double lonArrivee = Double.parseDouble(parts[5]);

                String nomRue = parts[6];
                double distance = Double.parseDouble(parts[7]); // distance directe depuis le fichier

                // Créer les nœuds si nécessaire
                Noeud depart = graphe.getNoeud(idDepart);
                if (depart == null) {
                    depart = new Noeud(idDepart, latDepart, lonDepart);
                    graphe.ajouterNoeud(depart);
                }

                Noeud arrivee = graphe.getNoeud(idArrivee);
                if (arrivee == null) {
                    arrivee = new Noeud(idArrivee, latArrivee, lonArrivee);
                    graphe.ajouterNoeud(arrivee);
                }

                // Ajouter l'arête avec la distance du fichier
                graphe.ajouterArete(depart, arrivee, distance, nomRue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }
}