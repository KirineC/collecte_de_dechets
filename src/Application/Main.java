package application;

import RéseauRoutier.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Plan de la ville fictive");
        Graphe graphe = LecteurTexte.chargerFichier("villeDepotParti.txt");
        System.out.println("Graphe chargé : " + graphe.getNoeuds().size() + " noeuds");

        // Définir le dépôt fixe
        String idDepot = "Depot";
        Noeud depot = graphe.getNoeud(idDepot);
        if (depot == null) {
            System.out.println("Le dépôt 'Depot' n'existe pas dans le graphe !");
            return;
        }

        // Demander le particulier à visiter
        System.out.print("\nID du particulier à visiter (P1 à P4) : ");
        String idMaison = sc.nextLine();
        Noeud maison = graphe.getNoeud(idMaison);

        if (maison == null) {
            System.out.println("ID du particulier invalide.");
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
                Arete ar = graphe.getArete(aller.get(i - 1), aller.get(i));
                if (ar != null) {
                    distanceAller += ar.getDistance();
                    System.out.println("   (Rue : " + ar.getNomRue() +
                            ", distance : " + ar.getDistance() + " m)");
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
                Arete ar = graphe.getArete(retour.get(i - 1), retour.get(i));
                if (ar != null) {
                    distanceRetour += ar.getDistance();
                    System.out.println("   (Rue : " + ar.getNomRue() +
                            ", distance : " + ar.getDistance() + " m)");
                }
            }
        }
        System.out.println("Distance retour : " + distanceRetour + " mètres");

        // Distance totale
        double distanceTotale = distanceAller + distanceRetour;
        System.out.println("\nDistance totale aller-retour : " + distanceTotale + " mètres");
    }
}