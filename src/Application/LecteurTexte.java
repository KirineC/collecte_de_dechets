package application;

import RéseauRoutier.*;
import java.io.*;

public class LecteurTexte {

    public static Graphe chargerFichier(String nomFichier) {
        Graphe graphe = new Graphe();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                // Format : idDepart;idArrivee;nomRue;distance;doubleSens
                String[] parts = ligne.split(";");
                if (parts.length != 5) continue;

                String idDepart = parts[0];
                String idArrivee = parts[1];
                String nomRue = parts[2];
                double distance = Double.parseDouble(parts[3]);
                boolean doubleSens = Boolean.parseBoolean(parts[4]);

                // Créer les nœuds si nécessaire
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

                // Ajouter l'arête avec info double sens
                graphe.ajouterArete(depart, arrivee, distance, nomRue, doubleSens);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }
}