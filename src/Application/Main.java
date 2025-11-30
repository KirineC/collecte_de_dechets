package application;

import RéseauRoutier.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez une ville :");
        System.out.println("1 - Ville fictive (ville.txt)");
        System.out.println("2 - Paris 15ème (paris.txt)");
        System.out.print("Votre choix : ");

        int choix = Integer.parseInt(sc.nextLine());

        String fichier;

        if (choix == 1) {
            fichier = "ville.txt";
        } else if (choix == 2) {
            fichier = "paris.txt";
        } else {
            System.out.println("Choix invalide.");
            return;
        }

        Graphe graphe = application.LecteurTexte.chargerFichier(fichier);


        System.out.println("Plan de la ville fictive");
        System.out.println("Graphe chargé : " + graphe.getNoeuds().size() + " noeuds");

        System.out.println("\nTournée des encombrants (aller-retour via plus court chemin)");
        System.out.print("ID du dépôt : ");
        String idDepot = sc.nextLine();

        System.out.print("ID de la maison à visiter : ");
        String idMaison = sc.nextLine();

        Noeud depot = graphe.getNoeud(idDepot);
        Noeud maison = graphe.getNoeud(idMaison);

        if (depot == null || maison == null) {
            System.out.println("ID invalide.");
            return;
        }

        // Calcul du chemin aller
        List<Noeud> aller = Dijkstra.plusCourtChemin(graphe, depot, maison);

        // Calcul du chemin retour
        List<Noeud> retour = Dijkstra.plusCourtChemin(graphe, maison, depot);

        // Affichage du trajet aller
        double distanceAller = 0.0;
        System.out.println("\nItinéraire aller :");
        for (int i = 0; i < aller.size(); i++) {
            System.out.println(" → " + aller.get(i).getId());

            if (i > 0) {
                Noeud a = aller.get(i - 1);
                Noeud b = aller.get(i);
                Arete ar = graphe.getArete(a, b);
                if (ar != null) {
                    distanceAller += ar.getDistance();
                    System.out.println("   (Rue : " + ar.getNomRue() +
                            ", distance : " + ar.getDistance() + ")");
                }
            }
        }
        System.out.println("Distance aller : " + distanceAller + " mètres");

        // Affichage du trajet retour
        double distanceRetour = 0.0;
        System.out.println("\nItinéraire retour :");
        for (int i = 0; i < retour.size(); i++) {
            System.out.println(" → " + retour.get(i).getId());

            if (i > 0) {
                Noeud a = retour.get(i - 1);
                Noeud b = retour.get(i);
                Arete ar = graphe.getArete(a, b);
                if (ar != null) {
                    distanceRetour += ar.getDistance();
                    System.out.println("   (Rue : " + ar.getNomRue() +
                            ", distance : " + ar.getDistance() + ")");
                }
            }
        }
        System.out.println("Distance retour : " + distanceRetour + " mètres");

        // Distance totale
        double distanceTotale = distanceAller + distanceRetour;
        System.out.println("\nDistance totale aller-retour : " + distanceTotale + " mètres");
    }
}