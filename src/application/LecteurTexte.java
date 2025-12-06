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
                if (parts.length != 5) continue;

                String idDepart = parts[0].trim();
                String idArrivee = parts[1].trim();
                double distance = Double.parseDouble(parts[2].trim());
                String typeRue = parts[3].trim().toLowerCase();
                int sens = Integer.parseInt(parts[4].trim());

                Noeud depart = graphe.getNoeud(idDepart);
                if (depart == null) { depart = new Noeud(idDepart); graphe.ajouterNoeud(depart); }

                Noeud arrivee = graphe.getNoeud(idArrivee);
                if (arrivee == null) { arrivee = new Noeud(idArrivee); graphe.ajouterNoeud(arrivee); }

                boolean estOriente;
                if (typeRue.equals("simple"))
                    estOriente = true;   // sens unique
                else
                    estOriente = false;  // double sens

                String nomRue = "Rue" + compteurRue++;
                graphe.ajouterArete(depart, arrivee, distance, nomRue, estOriente);//
                // POUR H03 graphe.ajouterArete(depart, arrivee, distance, nomRue, sens);
            }
        } catch (IOException e) { e.printStackTrace(); }

        return graphe;
    }
}