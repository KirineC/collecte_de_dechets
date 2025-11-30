package application;

import RéseauRoutier.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Graphe graphe = LecteurTexte.chargerFichier("H02H03.txt");
        System.out.println("Graphe chargé : " + graphe.getNoeuds().size() + " noeuds");

        Noeud depot = graphe.getNoeud("Depot");
        if (depot == null) {
            System.out.println("Le dépôt 'Depot' n'existe pas !");
            return;
        }

        System.out.println("\nChoix du mode de ramassage :");
        System.out.println("1 - Un seul particulier");
        System.out.println("2 - Plusieurs particuliers (tournée)");
        System.out.print("Votre choix : ");
        int choix = Integer.parseInt(sc.nextLine());

        if (choix == 1) {
            System.out.print("\nID du particulier à visiter : ");
            String idMaison = sc.nextLine();
            Noeud maison = graphe.getNoeud(idMaison);
            if (maison == null) {
                System.out.println("ID invalide.");
                return;
            }

            afficherChemin(graphe, depot, maison);
            afficherChemin(graphe, maison, depot);

        } else if (choix == 2) {
            System.out.print("\nListe des particuliers (ex: P1,P2,P3) : ");
            String[] ids = sc.nextLine().split(",");
            List<Noeud> particuliers = new ArrayList<>();
            for (String id : ids) {
                Noeud n = graphe.getNoeud(id.trim());
                if (n != null) particuliers.add(n);
            }
            if (particuliers.isEmpty()) return;

            // Méthode gloutonne pour tournée
            List<Noeud> tournee = new ArrayList<>();
            Set<Noeud> nonVisites = new HashSet<>(particuliers);
            Noeud courant = depot;
            tournee.add(depot);

            while (!nonVisites.isEmpty()) {
                Noeud plusProche = null;
                double distMin = Double.MAX_VALUE;

                for (Noeud n : nonVisites) {
                    List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, courant, n);
                    double dist = 0;
                    for (int i = 1; i < chemin.size(); i++) {
                        Arete ar = graphe.getArete(chemin.get(i-1), chemin.get(i));
                        dist += (ar != null ? ar.getDistance() : 0);
                    }
                    if (dist < distMin) {
                        distMin = dist;
                        plusProche = n;
                    }
                }

                tournee.add(plusProche);
                nonVisites.remove(plusProche);
                courant = plusProche;
            }
            tournee.add(depot);

            // Affichage complet de la tournée
            double distanceTotale = 0;
            System.out.println("\nTournée optimisée :");
            for (int i = 1; i < tournee.size(); i++) {
                List<Noeud> segment = Dijkstra.plusCourtChemin(graphe, tournee.get(i-1), tournee.get(i));
                for (int j = 1; j < segment.size(); j++) {
                    Arete ar = graphe.getArete(segment.get(j-1), segment.get(j));
                    distanceTotale += (ar != null ? ar.getDistance() : 0);
                    System.out.println(" → " + segment.get(j).getId() + " (Rue : " + (ar!=null?ar.getNomRue():"") + ", " + (ar!=null?ar.getDistance():"") + " m)");
                }
            }
            System.out.println("\nDistance totale de la tournée : " + distanceTotale + " mètres");
        }
    }

    private static void afficherChemin(Graphe graphe, Noeud depart, Noeud arrivee) {
        List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, depart, arrivee);
        double distanceTotale = 0;
        System.out.println("\nChemin de " + depart.getId() + " à " + arrivee.getId() + " :");
        for (int i = 1; i < chemin.size(); i++) {
            Arete ar = graphe.getArete(chemin.get(i-1), chemin.get(i));
            distanceTotale += (ar != null ? ar.getDistance() : 0);
            System.out.println(" → " + chemin.get(i).getId() + " (Rue : " + (ar!=null?ar.getNomRue():"") + ", " + (ar!=null?ar.getDistance():"") + " m)");
        }
        System.out.println("Distance totale : " + distanceTotale + " m");
    }
}