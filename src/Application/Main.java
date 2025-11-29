package application;

import RéseauRoutier.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Plan de la ville fictive");
        Graphe graphe = LecteurTexte.chargerFichier("ville.txt");
        System.out.println("Graphe chargé : " + graphe.getNoeuds().size() + " noeuds");

        System.out.println("\nTournée des encombrants (via plus court chemin)");
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

        List<Noeud> trajet = Dijkstra.plusCourtChemin(graphe, depot, maison);

        System.out.println("\nItinéraire optimal :");
        for (Noeud n : trajet) {
            System.out.println(" → " + n.getId());
        }
    }
}