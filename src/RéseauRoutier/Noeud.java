package RéseauRoutier;

public class Noeud {
    private String id;

    public Noeud(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    @Override
    public String toString() {
        return "Noeud " + id ;
    }
}
