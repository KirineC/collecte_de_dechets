package RéseauRoutier;

public class Arete {
    private Noeud depart;
    private Noeud arrivee;
    private double distance;
    private String nomRue;
    private boolean estOriente;

    public Arete(Noeud depart, Noeud arrivee, double distance, String nomRue, boolean estOriente) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
        this.nomRue = nomRue;
        this.estOriente = estOriente;
    }

    public Noeud getDepart() { return depart; }
    public Noeud getArrivee() { return arrivee; }
    public double getDistance() { return distance; }
    public String getNomRue() { return nomRue; }
    public boolean estOriente() { return estOriente; }
}
