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
        System.out.println("\nChoix du mode de ramassage :");
        System.out.println("1 - Un seul particulier");
        System.out.println("2 - Plusieurs particuliers (tournée)");
        System.out.print("Votre choix : ");
        int choix = Integer.parseInt(sc.nextLine());

        if (choix == 1) {
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
        else if (choix == 2) {
            System.out.print("\nListe des particuliers à visiter (ex: P1,P2,P3) : ");
            String ligne = sc.nextLine();
            String[] ids = ligne.split(",");
            List<Noeud> particuliers = new ArrayList<>();

            for (String id : ids) {
                id = id.trim();
                if (id.isEmpty()) continue;
                Noeud n = graphe.getNoeud(id);
                if (n != null) {
                    particuliers.add(n);
                } else {
                    System.out.println("Attention : " + id + " n'existe pas et sera ignoré.");
                }
            }


            if (particuliers.isEmpty()) {
                System.out.println("Aucun particulier valide sélectionné.");
                return;
            }

            List<Noeud> tournee = new ArrayList<>();
            Set<Noeud> nonVisites = new HashSet<>(particuliers);
            Noeud courant = depot;
            tournee.add(depot);

            while (!nonVisites.isEmpty()) {
                Noeud plusProche = null;
                double distMin = Double.MAX_VALUE;

                for (Noeud n : nonVisites) {
                    List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, courant, n);

                    double dist = 0.0;
                    for (int i = 1; i < chemin.size(); i++) {
                        Arete ar = graphe.getArete(chemin.get(i - 1), chemin.get(i));
                        if (ar != null) {
                            dist += ar.getDistance();
                        }
                    }

                    if (dist < distMin) {
                        distMin = dist;
                        plusProche = n;
                    }
                }


                if (plusProche == null) {
                    System.out.println("Erreur : impossible de trouver un chemin vers certains particuliers.");
                    break;
                }

                tournee.add(plusProche);
                nonVisites.remove(plusProche);
                courant = plusProche;
            }


            tournee.add(depot);

            double distanceTotale = 0.0;
            System.out.println("\nTournée optimisée :");
            for (int i = 1; i < tournee.size(); i++) {
                List<Noeud> segment = Dijkstra.plusCourtChemin(graphe, tournee.get(i - 1), tournee.get(i));
                for (int j = 0; j < segment.size(); j++) {
                    System.out.println(" → " + segment.get(j).getId());
                    if (j > 0) {
                        Arete ar = graphe.getArete(segment.get(j - 1), segment.get(j));
                        if (ar != null) {
                            distanceTotale += ar.getDistance();
                            System.out.println("   (Rue : " + ar.getNomRue() + ", distance : " + ar.getDistance() + " m)");
                        }
                    }
                }
            }
            System.out.println("\nDistance totale de la tournée : " + distanceTotale + " mètres");
        }




    }
}