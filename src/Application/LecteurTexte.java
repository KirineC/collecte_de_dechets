package application;

import RéseauRoutier.*;
import java.io.*;
import java.util.*;

public class LecteurTexte {

    public static Graphe chargerFichier(String nomFichier) {
        Graphe graphe = new Graphe();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                // Format : idDepart;latDepart;lonDepart;idArrivee;latArrivee;lonArrivee;nomRue
                String[] parts = ligne.split(";");
                if (parts.length != 7) continue;

                String idDepart = parts[0];
                double latDepart = Double.parseDouble(parts[1]);
                double lonDepart = Double.parseDouble(parts[2]);

                String idArrivee = parts[3];
                double latArrivee = Double.parseDouble(parts[4]);
                double lonArrivee = Double.parseDouble(parts[5]);

                String nomRue = parts[6];

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

                // Calcul distance euclidienne
                double distance = calculerDistance(depart, arrivee);

                // Ajouter l'arête
                graphe.ajouterArete(depart, arrivee, distance, nomRue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }

    private static double calculerDistance(Noeud a, Noeud b) {
        double dx = a.getLatitude() - b.getLatitude();
        double dy = a.getLongitude() - b.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }
}