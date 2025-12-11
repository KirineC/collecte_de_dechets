package application;

import RéseauRoutier.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Graphe graphe = LecteurTexte.chargerFichier("H2P15.txt");
        System.out.println("Graphe chargé : " + graphe.getNoeuds().size() + " noeuds");

        Noeud depot = graphe.getNoeud("Depot");
        if (depot == null) { System.out.println("Le dépôt 'Depot' n'existe pas !"); return; }
        while (true){
        System.out.println("\nChoix du mode de ramassage :\n1 - Collecte des encombrants chez un particulier\n2 - Tournée de ramassages des encombrants\n3 - Collecte complète des poubelles aux pieds des habitations\n4 - Quitter");
        System.out.print("Votre choix : ");
        int choix = Integer.parseInt(sc.nextLine());

        if (choix == 1) {
            System.out.print("\nID du particulier à visiter : ");
            Noeud maison = graphe.getNoeud(sc.nextLine().trim());
            if (maison == null) { System.out.println("ID invalide."); return; }

            double distanceAller = afficherTrajet(graphe, depot, maison, "aller");
            double distanceRetour = afficherTrajet(graphe, maison, depot, "retour");
            System.out.println("\nDistance totale aller-retour : " + (distanceAller + distanceRetour) + " m");

        } else if (choix == 2) {
            Map<String, List<Noeud>> tournéesParDate = new LinkedHashMap<>();

            while (true) {
                System.out.print("\nEntrer la date de la tournée (ou 'fin' pour terminer) : ");
                String date = sc.nextLine().trim();
                if (date.equalsIgnoreCase("fin")) break;

                System.out.print("Liste des particuliers pour " + date + " (max 10, ex: P1,P2,...,P10) : ");
                String[] ids = sc.nextLine().split(",");
                if (ids.length > 10) {
                    System.out.println("Vous ne pouvez sélectionner que 10 particuliers au maximum !");
                    continue; // redemande la saisie
                }

                List<Noeud> particuliers = new ArrayList<>();
                for (String id : ids) {
                    Noeud n = graphe.getNoeud(id.trim());
                    if (n != null) particuliers.add(n);
                }
                if (particuliers.isEmpty()) {
                    System.out.println("Aucun particulier valide pour cette date !");
                    continue;
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
                        double dist = 0;
                        for (int i = 1; i < chemin.size(); i++) {
                            Arete ar = graphe.getArete(chemin.get(i-1), chemin.get(i));
                            dist += (ar != null ? ar.getDistance() : 0);
                        }
                        if (dist < distMin) { distMin = dist; plusProche = n; }
                    }

                    tournee.add(plusProche);
                    nonVisites.remove(plusProche);
                    courant = plusProche;
                }
                tournee.add(depot);

                tournéesParDate.put(date, tournee);
            }

            System.out.println("\ntournées par date");
            for (String date : tournéesParDate.keySet()) {
                System.out.println("\nDate : " + date);
                List<Noeud> tournee = tournéesParDate.get(date);
                double distanceTotale = 0;
                for (int i = 1; i < tournee.size(); i++) {
                    distanceTotale += afficherTrajet(graphe, tournee.get(i-1), tournee.get(i), "segment " + i);
                }
                System.out.println("Distance totale de la tournée : " + distanceTotale + " m");
            }
        }

        else if (choix == 3) {
            Eulerien euler = new Eulerien(graphe);
            List<Arete> circuit = euler.calculerCircuitGeneral(depot);
            System.out.println("\nCircuit eulérien :");
            double distanceTotale = 0;
            Noeud precedent = depot;
            for (Arete ar : circuit) {
                if (ar.getDepart().equals(precedent)) {
                    System.out.println(precedent.getId() + " -> " + ar.getArrivee().getId()
                            + " (Rue : " + ar.getNomRue() + ")");
                    precedent = ar.getArrivee();
                } else {
                    // on a parcouru l'arête dans l'autre sens
                    System.out.println(precedent.getId() + " -> " + ar.getDepart().getId()
                            + " (Rue : " + ar.getNomRue() + ")");
                    precedent = ar.getDepart();
                }
                distanceTotale += ar.getDistance();
            }
            System.out.println("Distance totale : " + distanceTotale + " m");
        }
        else if (choix == 4) {
            break ;
        }
        }

    }

    private static double afficherTrajet(Graphe graphe, Noeud depart, Noeud arrivee, String label) {
        List<Noeud> chemin = Dijkstra.plusCourtChemin(graphe, depart, arrivee);
        double distanceTotale = 0;
        System.out.println("\nTrajet " + label + " de " + depart.getId() + " à " + arrivee.getId() + " :");
        for (int i = 1; i < chemin.size(); i++) {
            Arete ar = graphe.getArete(chemin.get(i-1), chemin.get(i));
            distanceTotale += (ar != null ? ar.getDistance() : 0);
            System.out.println(" → " + chemin.get(i).getId()
                    + " (Rue : " + (ar != null ? ar.getNomRue() : "")
                    + ", " + (ar != null ? ar.getDistance() : "") + " m)");
        }
        System.out.println("Distance " + label + " : " + distanceTotale + " m");
        return distanceTotale;
    }
}