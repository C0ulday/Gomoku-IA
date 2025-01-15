public class coordonnee {
    private int x;
    private int y;

    // Constructeur
    public coordonnee(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter pour la coordonnée X
    public int getX() {
        return x;
    }

    // Getter pour la coordonnée Y
    public int getY() {
        return y;
    }

    // Méthode pour afficher les coordonnées (optionnel)
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
