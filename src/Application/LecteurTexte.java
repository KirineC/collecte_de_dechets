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
                if (parts.length != 3) continue;

                String idDepart = parts[0].trim();
                String idArrivee = parts[1].trim();
                double distance = Double.parseDouble(parts[2].trim());

                // Générer un nom de rue unique
                String nomRue = "Rue" + compteurRue;
                compteurRue++;

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
                // Ajouter l'arête
                graphe.ajouterArete(depart, arrivee, distance, nomRue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
    }
}