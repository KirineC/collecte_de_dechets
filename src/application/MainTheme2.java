package application;

import RéseauRoutier.*;
import algo.MST;
import algo.NearestNeighborTSP;

import java.util.*;
import java.util.stream.Collectors;

public class MainTheme2 {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            // 1) Charger le graphe
            Graphe g = LecteurTexteT2.chargerFichier("Vendome.txt");
            System.out.println("Graphe chargé (Thème 2) : " + g.getNoeuds().size() + " noeuds");

            String depot = "Depot";
            Set<String> allVertices = g.getNoeuds().keySet();
            if (!allVertices.contains(depot)) {
                System.out.println("Le dépôt '" + depot + "' n'existe pas dans le graphe !");
                return;
            }

            // 2) Extraire les points P1, P2, ...
            List<String> pointsCollecte = allVertices.stream()
                    .filter(name -> name.matches("P\\d+"))
                    .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.substring(1))))
                    .collect(Collectors.toList());

            List<String> tspNodes = new ArrayList<>();
            tspNodes.add(depot);
            tspNodes.addAll(pointsCollecte);

            // 📌 Construction de la matrice des distances UNE FOIS !
            int n = tspNodes.size();
            double[][] dist = new double[n][n];
            for (int i = 0; i < n; i++) {
                String sourceName = tspNodes.get(i);
                Noeud sourceNode = g.getNoeud(sourceName);

                Dijkstra.Result res = Dijkstra.shortestPaths(g, sourceNode);

                for (int j = 0; j < n; j++) {
                    Double d = res.dist.get(g.getNoeud(tspNodes.get(j)));
                    dist[i][j] = (d != null ? d : Double.POSITIVE_INFINITY);
                }
            }

            // ================================
            //     MENU INTERACTIF THEME 2
            // ================================
            while (true) {
                System.out.println("\n============== MENU TSP (Thème 2) ==============");
                System.out.println("1 - Approche 1 : Plus proche voisin (Nearest Neighbor)");
                System.out.println("2 - Approche 2 : MST + DFS");
                System.out.println("3 - Retour au menu principal");
                System.out.print("Votre choix : ");

                int choix = Integer.parseInt(sc.nextLine().trim());

                if (choix == 1) {
                    approcheNN(tspNodes, dist, depot);
                }
                else if (choix == 2) {
                    approcheMST(g, tspNodes, dist, depot);
                }
                else if (choix == 3) {
                    break;
                }
                else {
                    System.out.println("Choix invalide.");
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur dans le thème 2 :");
            e.printStackTrace();
        }
    }

    // ==============================
    // APPROCHE 1 : PLUS PROCHE VOISIN
    // ==============================
    private static void approcheNN(List<String> tspNodes, double[][] dist, String depot) {
        System.out.println("\n================= APPROCHE 1 : Plus proche voisin =================");
        List<String> tourNN = NearestNeighborTSP.tour(tspNodes, depot, dist);
        double lengthNN = NearestNeighborTSP.tourLength(tourNN, tspNodes, dist);

        System.out.println("Tournée (NN) : " + String.join(" -> ", tourNN));
        System.out.println("Distance totale approx : " + lengthNN);
    }

    // ==============================
    // APPROCHE 2 : MST + DFS + CAPACITÉ
    // ==============================
    private static void approcheMST(Graphe g, List<String> tspNodes, double[][] dist, String depot) {
        System.out.println("\n================= APPROCHE 2 : MST =================");

        int depotIndex = tspNodes.indexOf(depot);
        int[] parent = MST.prim(dist, depotIndex);
        List<List<Integer>> tree = MST.buildTreeFromParent(parent);
        List<Integer> preorder = MST.dfsPreorder(tree, depotIndex);

        List<String> tourMST = new ArrayList<>();
        for (int idx : preorder) tourMST.add(tspNodes.get(idx));
        if (!tourMST.get(tourMST.size() - 1).equals(depot)) {
            tourMST.add(depot);
        }

        double lengthMST = NearestNeighborTSP.tourLength(tourMST, tspNodes, dist);

        System.out.println("Ordre de visite : " + String.join(" -> ", tourMST));
        System.out.println("Distance totale approx (MST) : " + lengthMST);

        // ===== CAPACITÉ =====
        int C = 20;
        System.out.println("\nCAPACITÉ DU CAMION = " + C);

        List<List<String>> tournees = new ArrayList<>();
        List<String> currentTour = new ArrayList<>();
        currentTour.add(depot);

        int charge = 0;
        for (String point : tourMST) {
            if (point.equals(depot)) continue;
            int c = g.getCapacity(point);

            if (charge + c <= C) {
                currentTour.add(point);
                charge += c;
            } else {
                currentTour.add(depot);
                tournees.add(new ArrayList<>(currentTour));
                currentTour.clear();
                currentTour.add(depot);
                currentTour.add(point);
                charge = c;
            }
        }

        currentTour.add(depot);
        tournees.add(currentTour);

        System.out.println("\nTournées réelles obtenues :");
        int num = 1;
        for (List<String> tour : tournees) {
            int totalCharge = tour.stream()
                    .filter(p -> !p.equals(depot))
                    .mapToInt(g::getCapacity)
                    .sum();

            System.out.println("T" + num + " : " + String.join(" -> ", tour)
                    + " | Charge = " + totalCharge + " / " + C);
            num++;
        }
    }
}
