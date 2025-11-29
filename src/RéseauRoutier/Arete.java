package RéseauRoutier;

public class Arete {
    private Noeud depart;
    private Noeud arrivee;
    private double distance;
    private String nomRue;
    private boolean doubleSens; // nouveau champ

    public Arete(Noeud depart, Noeud arrivee, double distance, String nomRue) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
        this.nomRue = nomRue;
        this.doubleSens = doubleSens;
    }

    public Noeud getDepart() { return depart; }
    public Noeud getArrivee() { return arrivee; }
    public double getDistance() { return distance; }
    public String getNomRue() { return nomRue; }
    public boolean isDoubleSens() { return doubleSens; } // getter pour doubleSens
}