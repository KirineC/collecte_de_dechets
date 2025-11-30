package RéseauRoutier;

public class Noeud {
    private String id;

    public Noeud(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noeud noeud = (Noeud) o;
        return id.equals(noeud.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Noeud " + id ;
    }
}
