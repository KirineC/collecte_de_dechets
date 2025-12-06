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
            String depot = "Depot";

            while (true) {
                System.out.println("\n============== MENU TSP (Thème 2) ==============");
                System.out.println("1 - Approche 1 : Plus proche voisin (Nearest Neighbor)");
                System.out.println("2 - Approche 2 : MST + DFS (+ capacité)");
                System.out.println("3 - Quitter");
                System.out.print("Votre choix : ");

                int choix;
                try {
                    choix = Integer.parseInt(sc.nextLine().trim());
                } catch (Exception e) {
                    System.out.println("Choix invalide.");
                    continue;
                }

                if (choix == 3) {
                    System.out.println("Fin du Thème 2.");
                    break;
                }

                // === 1) Choisir l'hypothèse d'orientation (HO1/HO2/HO3) ===
                int ho = demanderHypothese(sc);

                // === 2) Choisir le fichier correspondant ===
                String filename;
                switch (ho) {
                    case 1: filename = "Vendome.txt";    break;
                    case 2: filename = "Vendomeho2.txt"; break;
                    case 3: filename = "VendomeHO3.txt"; break;
                    default: filename = "Vendome.txt";   // sécurité
                }

                // === 3) Charger le graphe ===
                Graphe g = LecteurTexteT2.chargerFichier(filename, ho);
                System.out.println("\n=== Fichier chargé : " + filename + " (HO" + ho + ") ===");
                System.out.println("Nombre de noeuds : " + g.getNoeuds().size());

                Set<String> allVertices = g.getNoeuds().keySet();
                if (!allVertices.contains(depot)) {
                    System.out.println("Le dépôt '" + depot + "' n'existe pas dans le graphe !");
                    continue; // retour au menu principal
                }

                // === 4) Préparation des sommets TSP : Depot + P1, P2, ... ===
                List<String> pointsCollecte = allVertices.stream()
                        .filter(name -> name.matches("P\\d+"))
                        .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.substring(1))))
                        .collect(Collectors.toList());

                if (pointsCollecte.isEmpty()) {
                    System.out.println("Aucun point de collecte P1, P2, ... trouvé dans ce graphe.");
                    continue;
                }

                List<String> tspNodes = new ArrayList<>();
                tspNodes.add(depot);
                tspNodes.addAll(pointsCollecte);

                int n = tspNodes.size();
                double[][] dist = new double[n][n];

                // Map des chemins détaillés : chemins.get(source).get(cible) = liste des sommets
                Map<String, Map<String, List<String>>> chemins = new HashMap<>();

                // === 5) Construction de la matrice de distances avec Dijkstra ===
                for (int i = 0; i < n; i++) {
                    String sourceName = tspNodes.get(i);
                    Noeud sourceNode = g.getNoeud(sourceName);

                    Dijkstra.Result res = Dijkstra.shortestPaths(g, sourceNode);

                    Map<String, List<String>> cheminsDepuisSource = new HashMap<>();

                    for (int j = 0; j < n; j++) {
                        String targetName = tspNodes.get(j);
                        Noeud targetNode = g.getNoeud(targetName);

                        Double d = res.dist.get(targetNode);
                        dist[i][j] = (d != null ? d : Double.POSITIVE_INFINITY);

                        if (d != null && !Double.isInfinite(d)) {
                            List<Noeud> pathNodes = res.buildPath(sourceNode, targetNode);
                            if (pathNodes != null && !pathNodes.isEmpty()) {
                                List<String> pathIds = new ArrayList<>();
                                for (Noeud nd : pathNodes) {
                                    pathIds.add(nd.getId());
                                }
                                cheminsDepuisSource.put(targetName, pathIds);
                            }
                        }
                    }

                    chemins.put(sourceName, cheminsDepuisSource);
                }

                // === 6) Lancer l'approche choisie ===
                if (choix == 1) {
                    approcheNN(g, tspNodes, dist, depot, filename, ho, chemins);
                } else if (choix == 2) {
                    approcheMST(g, tspNodes, dist, depot, filename, ho, chemins);
                } else {
                    System.out.println("Choix invalide.");
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur dans le thème 2 :");
            e.printStackTrace();
        }
    }

    // ==============================
    // Demander HO1 / HO2 / HO3
    // ==============================
    private static int demanderHypothese(Scanner sc) {
        while (true) {
            System.out.println("\nChoisissez l'hypothèse d'orientation :");
            System.out.println("1 - HO1 : Graphe non orienté (double sens)");
            System.out.println("2 - HO2 : Graphe orienté (sens uniques)");
            System.out.println("3 - HO3 : Graphe mixte");
            System.out.print("Votre choix : ");

            try {
                int ho = Integer.parseInt(sc.nextLine().trim());
                if (ho >= 1 && ho <= 3) {
                    return ho;
                }
            } catch (Exception e) {
                // ignore, on reboucle
            }
            System.out.println("Choix invalide, recommencez.");
        }
    }

    // ==============================
    // APPROCHE 1 : PLUS PROCHE VOISIN
    // ==============================
    private static void approcheNN(Graphe g,
                                   List<String> tspNodes,
                                   double[][] dist,
                                   String depot,
                                   String filename,
                                   int ho,
                                   Map<String, Map<String, List<String>>> chemins) {
        System.out.println("\n================= APPROCHE 1 : Plus proche voisin =================");
        System.out.println("Fichier : " + filename + " | HO" + ho);

        List<String> tourNN = NearestNeighborTSP.tour(tspNodes, depot, dist);
        double lengthNN = NearestNeighborTSP.tourLength(tourNN, tspNodes, dist);

        System.out.println("Tournée (NN) sur les points de collecte :");
        System.out.println(String.join(" -> ", tourNN));
        System.out.println("Distance totale approx : " + lengthNN);

        // Chemin complet sommet par sommet avec contrainte "pas de demi-tour sur Pi"
        List<String> cheminComplet = construireCheminComplet(g, tourNN, chemins);
        System.out.println("Chemin détaillé (sommet par sommet) :");
        System.out.println(String.join(" -> ", cheminComplet));
    }

    // ==============================
    // APPROCHE 2 : MST + DFS + CAPACITÉ
    // ==============================
    private static void approcheMST(Graphe g,
                                    List<String> tspNodes,
                                    double[][] dist,
                                    String depot,
                                    String filename,
                                    int ho,
                                    Map<String, Map<String, List<String>>> chemins) {
        System.out.println("\n================= APPROCHE 2 : MST =================");
        System.out.println("Fichier : " + filename + " | HO" + ho);

        int depotIndex = tspNodes.indexOf(depot);
        if (depotIndex < 0) {
            System.out.println("Erreur : le dépôt n'est pas dans tspNodes.");
            return;
        }

        // 1) Arbre couvrant de poids minimum (Prim)
        int[] parent = MST.prim(dist, depotIndex);

        // 2) Construire l'arbre sous forme de liste d'adjacence
        List<List<Integer>> tree = MST.buildTreeFromParent(parent);

        // 3) Parcours en profondeur préfixe (DFS) depuis le dépôt
        List<Integer> preorder = MST.dfsPreorder(tree, depotIndex);

        // 4) Déduire l'ordre de visite des sommets TSP
        List<String> tourMST = new ArrayList<>();
        for (int idx : preorder) {
            tourMST.add(tspNodes.get(idx));
        }
        // On revient au dépôt à la fin si ce n'est pas déjà le cas
        if (!tourMST.get(tourMST.size() - 1).equals(depot)) {
            tourMST.add(depot);
        }

        double lengthMST = NearestNeighborTSP.tourLength(tourMST, tspNodes, dist);

        System.out.println("Ordre de visite (points) :");
        System.out.println(String.join(" -> ", tourMST));
        System.out.println("Distance totale approx (MST) : " + lengthMST);

        // Chemin complet sommet par sommet avec contrainte "pas de demi-tour sur Pi"
        List<String> cheminComplet = construireCheminComplet(g, tourMST, chemins);
        System.out.println("Chemin détaillé (sommet par sommet - MST) :");
        System.out.println(String.join(" -> ", cheminComplet));

        // ===== CAPACITÉ =====
        int C = 20;  // capacité maximale du camion (à adapter si besoin)
        System.out.println("\nCAPACITÉ DU CAMION = " + C);

        List<List<String>> tournees = new ArrayList<>();
        List<String> currentTour = new ArrayList<>();
        currentTour.add(depot);

        int charge = 0;
        for (String point : tourMST) {
            if (point.equals(depot)) continue; // on ne compte pas le dépôt dans la charge

            int c = g.getCapacity(point); // 0 si ce n’est pas un Pi
            if (c == 0) {
                // Ce n'est pas un point de collecte, on l'ignore pour le découpage en tournées
                continue;
            }

            if (charge + c <= C) {
                currentTour.add(point);
                charge += c;
            } else {
                // On ferme la tournée courante en revenant au dépôt
                currentTour.add(depot);
                tournees.add(new ArrayList<>(currentTour));

                // On démarre une nouvelle tournée
                currentTour.clear();
                currentTour.add(depot);
                currentTour.add(point);
                charge = c;
            }
        }

        // Si la dernière tournée n'est pas vide, on la ferme
        if (!currentTour.isEmpty()) {
            if (!currentTour.get(currentTour.size() - 1).equals(depot)) {
                currentTour.add(depot);
            }
            tournees.add(currentTour);
        }

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

    // ==============================
    // Construction du chemin complet
    // avec interdiction du demi-tour sur Pi
    // ==============================
    private static List<String> construireCheminComplet(Graphe g,
                                                        List<String> tour,
                                                        Map<String, Map<String, List<String>>> chemins) {
        List<String> cheminComplet = new ArrayList<>();
        if (tour == null || tour.size() < 2) return cheminComplet;

        for (int k = 0; k < tour.size() - 1; k++) {
            String from = tour.get(k);
            String to   = tour.get(k + 1);

            Map<String, List<String>> fromMap = chemins.get(from);
            if (fromMap == null) continue;
            List<String> segment = fromMap.get(to);
            if (segment == null || segment.isEmpty()) continue;

            // Vérification de la contrainte :
            // si on est sur un point de collecte et que le segment commence
            // par un retour immédiat vers le sommet précédent, on l'interdit.
            if (!cheminComplet.isEmpty() && cheminComplet.size() >= 2) {
                String avant = cheminComplet.get(cheminComplet.size() - 2);
                String dernier = cheminComplet.get(cheminComplet.size() - 1);
                // dernier doit être == from (jonction)
                if (dernier.equals(from) && estPointCollecte(dernier)) {
                    if (segment.size() >= 2 && segment.get(0).equals(from) && segment.get(1).equals(avant)) {
                        // On essaie un chemin alternatif qui interdit l'arc from -> avant
                        List<String> alternatif = plusCourtCheminSansArc(g, from, to, from, avant);
                        if (alternatif != null && !alternatif.isEmpty()) {
                            segment = alternatif;
                        }
                    }
                }
            }

            if (cheminComplet.isEmpty()) {
                cheminComplet.addAll(segment);
            } else {
                // éviter de répéter le sommet de jonction
                cheminComplet.remove(cheminComplet.size() - 1);
                cheminComplet.addAll(segment);
            }
        }
        return cheminComplet;
    }

    // Un point de collecte est de la forme P1, P2, ...
    private static boolean estPointCollecte(String id) {
        return id != null && id.matches("P\\d+");
    }

    /**
     * Plus court chemin source -> cible en interdisant l'arc interditFrom -> interditTo
     * (petit Dijkstra ad hoc sur le graphe g)
     */
    private static List<String> plusCourtCheminSansArc(Graphe g,
                                                       String sourceId,
                                                       String targetId,
                                                       String interditFrom,
                                                       String interditTo) {
        Noeud source = g.getNoeud(sourceId);
        Noeud target = g.getNoeud(targetId);
        if (source == null || target == null) return Collections.emptyList();

        Map<Noeud, Double> dist = new HashMap<>();
        Map<Noeud, Noeud> prev = new HashMap<>();
        for (Noeud n : g.getNoeuds().values()) {
            dist.put(n, Double.POSITIVE_INFINITY);
            prev.put(n, null);
        }
        dist.put(source, 0.0);

        PriorityQueue<Map.Entry<Noeud, Double>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
        pq.add(new AbstractMap.SimpleEntry<>(source, 0.0));

        Set<Noeud> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            Map.Entry<Noeud, Double> entry = pq.poll();
            Noeud u = entry.getKey();
            if (!visited.add(u)) continue;
            if (u.equals(target)) break;

            List<Arete> adj = g.getAdjacence().getOrDefault(u, Collections.emptyList());
            for (Arete e : adj) {
                Noeud v = e.getArrivee();

                // On saute l'arc interdit
                if (u.getId().equals(interditFrom) && v.getId().equals(interditTo)) {
                    continue;
                }

                double newDist = dist.get(u) + e.getDistance();
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(new AbstractMap.SimpleEntry<>(v, newDist));
                }
            }
        }

        Double dFinal = dist.get(target);
        if (dFinal == null || dFinal.isInfinite()) {
            return Collections.emptyList();
        }

        // Reconstruction du chemin
        List<String> path = new ArrayList<>();
        Noeud curr = target;
        while (curr != null) {
            path.add(0, curr.getId());
            if (curr.equals(source)) break;
            curr = prev.get(curr);
        }
        return path;
    }
}
