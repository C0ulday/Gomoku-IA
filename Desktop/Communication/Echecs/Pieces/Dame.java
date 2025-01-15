import java.util.ArrayList;

public class Dame extends Piece implements regle_Piece {
    int xactu, xinit;
    int yactu, yinit;
    private Plateau plateau;

    public Dame(int xinit, int yinit, int xactu, int yactu, String couleur, Plateau plateau) {
        super("DAME", xinit, yinit, xactu, yactu, couleur, plateau);
        this.xactu = xactu;
        this.yactu = yactu;
        this.xinit = xinit;
        this.yinit = yinit;
        this.plateau = plateau;
    }

    @Override
    public ArrayList<coordonnee> casesPossibles(int xactu, int yactu) {
        ArrayList<coordonnee> coords = new ArrayList<>();
        
        // Directions de déplacement de la dame dans les coordonnées X et Y
        // X = de gauche à droite
        // Y = de haut en bas 

        int[][] directions = {
            {1, 1},   // Bas Droite
            {1, 0},   // Droite
            {1, -1},  // Haut Droite
            {0,-1},   // Haut
            {-1, -1}, // Haut Gauche
            {-1, 0},  // Gauche
            {-1, 1},  // Bas Gauche
            {0, 1}    // Bas
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
            
            if (piece != null && !piece.getCouleur().equals(this.getCouleur())) {
                // Si la pièce est d'une couleur différente, elle est prenable
                casesPrenables.add(coord);
            }
        }

        return casesPrenables;
    }

    @Override
    public void afficherCoordsPossibles(int xactu, int yactu) {
        ArrayList<coordonnee> coords = casesPossibles(xactu, yactu);
    
        System.out.println("Coordonnées possibles pour la Dame en ["+xactu+","+yactu+"] :");
        for (coordonnee coord : coords) {
            System.out.println("X : " + coord.getX() + ", Y : " + coord.getY());
        }
    }
}
