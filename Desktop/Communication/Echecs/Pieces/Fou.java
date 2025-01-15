import java.util.ArrayList;

public class Fou extends Piece implements regle_Piece {
    int xactu, xinit;
    int yactu, yinit;
    private Plateau plateau;

    /* Constructeur 
     * public Piece(String name, int positionXinit, int positionYinit, int positionX, int positionY, String couleur, Plateau plateau) 
    */
    public Fou(int xinit, int yinit, int xactu, int yactu, String couleur, Plateau plateau) {
        super("FOU", xinit, yinit, xactu, yactu, couleur, plateau);
        this.xactu = xactu;
        this.yactu = yactu;
        this.xinit = xinit;
        this.yinit = yinit;
        this.plateau = plateau;
    }

    /**
     * Verifie si le deplacement est possible
     * @param x L'abscisse d'arrive = les lettres
     * @param y l'ordonnee d'arrive = les chiffres
     * @return resultat du test
     */


    /*
     *** La fonction prend en entrée les positions actuelles de la pièce
     * Vérifie les limites
     * Les déplacements
     * Vérifie si la case qu'on veut aller est nulle ou contient une pièce adverse
     * Les types de déplacements de la pièce
     * On ne peut pas sauter une pièce adverse
     *** La fonction retourne les coordonnées possibles où on peut aller 
     */

    @Override
    public ArrayList<coordonnee> casesPossibles(int xactu, int yactu) {
        ArrayList<coordonnee> coords = new ArrayList<>();
        
        // Directions de déplacement du fou dans les coordonnées X et Y
        // X = de gauche à droite
        // Y = de haut en bas 

        int[][] directions = {
            {1, -1}, // haut gauche
            {1, 1},  // haut droite
            {-1, -1}, // bas  gauche
            {-1, 1}   // bas  droite
        };

        for (int[] direction : directions) {
            
            boolean valide = true; // La piece peut se déplacer tant que cette variable est vraie
            int Y = direction[0];
            int X = direction[1];
            
            int xnew = xactu;
            int ynew = yactu;
    
            // Parcours dans une direction jusqu'à la limite ou un obstacle
            while (valide) {
                xnew += Y;
                ynew += X;
    
                // Vérifier si la case est hors limites
                if (!plateau.estDansLesLimites(xnew, ynew)) {
                    valide = false; // Arrêt si hors des limites
                    break; // sortir de la boucle sans créer de pièce
                }
    
                // Récupérer la pièce sur la case
                Piece piece = plateau.getPiece(xnew, ynew);
                if (piece.getCouleur() == "NULL")  // si la case est vide
                {
                    // Case vide, ajoutée aux mouvements possibles
                    coords.add(new coordonnee(xnew, ynew));
                } 
                else
                {
                    // Case occupée par une pièce adverse, ajoutée et arrêt dans cette direction
                    coords.add(new coordonnee(xnew, ynew));
                    valide = false; // La direction est bloquée après la capture
                }
            }
        }
    
        return coords;
    }

    @Override
    public ArrayList<coordonnee> casesPrenable(int xactu, int yactu) {
        ArrayList<coordonnee> casesPrenables = new ArrayList<>();
        ArrayList<coordonnee> casesJouables = casesPossibles(xactu, yactu);

        for (coordonnee coord : casesJouables) {
            int x = coord.getX();
            int y = coord.getY();
            
            // Récupérer la pièce à la position (x, y)
            Piece piece = plateau.getPiece(x,y);
            if (!piece.getCouleur().equals(this.getCouleur()) || piece == null)  // si la case est vide
            {
                // Case vide, ajoutée aux mouvements possibles
                casesPrenables.add(coord);
            }
        }

        return casesPrenables;
    }

    @Override
    public void afficherCoordsPossibles(int xactu, int yactu) {
        ArrayList<coordonnee> coords = casesPossibles(xactu, yactu);
    
        System.out.println("Coordonnées possibles pour le Fou en ["+xactu+","+yactu+"] :");
        for (coordonnee coord : coords) {
            System.out.println("X : " + coord.getX() + ", Y : " + coord.getY());
        }
    }
}
