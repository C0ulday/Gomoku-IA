import java.util.ArrayList;

//TODO : Retirer les cases avec Echec

public class Roi extends Piece implements regle_Piece{
    int xactu, xinit;
    int yactu, yinit;
    private Plateau plateau;

    public Roi(int xinit, int yinit, int xactu, int yactu, String couleur, Plateau plateau) {
        super("ROI", xinit, yinit, xactu, yactu, couleur, plateau);
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

     @Override
     public ArrayList<coordonnee> casesPossibles(int xactu, int yactu) {
         ArrayList<coordonnee> coords = new ArrayList<>();
         
         // Directions de déplacement du roi
         int[][] directions = {
             {1, 1},   // Bas Droite
             {1, 0},   // Droite
             {1, -1},  // Haut Droite
             {0, -1},  // Haut
             {-1, -1}, // Haut Gauche
             {-1, 0},  // Gauche
             {-1, 1},  // Bas Gauche
             {0, 1}    // Bas
         };
     
         for (int[] direction : directions) {
             int xnew = xactu + direction[0];
             int ynew = yactu + direction[1];
     
             // Vérifie si la case est dans les limites du plateau
             if (plateau.estDansLesLimites(xnew, ynew)) {
                 Piece piece = plateau.getPiece(xnew, ynew);
                 
                 // Vérifie si la case est vide ou contient une pièce ennemie
                 if (piece == null || !piece.getCouleur().equals(this.getCouleur())) {
                     // Simule le déplacement et vérifie si le Roi est toujours en sécurité
                     if (deplacementSecurise(this, xnew, ynew)) {
                         coords.add(new coordonnee(xnew, ynew));
                     }
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

    public boolean deplacementSecurise(Piece roi, int xnew, int ynew) {
        ArrayList<Piece> pieces = plateau.getPlateauPiece();
        
        for (Piece piece : pieces) {
            // Vérifie si la pièce est adverse
            if (!piece.getCouleur().equals(roi.getCouleur())) 
            {
                // Vérifie si la pièce adverse peut attaquer la position cible
                if (piece instanceof regle_Piece) {
                    ArrayList<coordonnee> casesAdverses = ((regle_Piece) piece).casesPossibles(piece.getPositionX(),piece.getPositionY());
    
                    // Vérifie si la position cible est attaquée
                    for (coordonnee coordAdverse : casesAdverses) {
                        if (coordAdverse.getX() == xnew && coordAdverse.getY() == ynew) {
                            return false; // Le déplacement met le roi en échec
                        }
                    }
                }
    
                // Cas particulier pour les pions (les pions attaquent en diagonale)
                if (piece.getName().equals("PION")) {
                    int direction = piece.getCouleur().equals("BLANC") ? -1 : 1; // Sens d'attaque du pion
                    int pionX = piece.getPositionX();
                    int pionY = piece.getPositionY();
    
                    if ((pionX + direction == xnew && pionY + 1 == ynew) ||
                        (pionX + direction == xnew && pionY - 1 == ynew)) {
                        return false; // Le pion attaque la case cible
                    }
                }
            }
        }
    
        return true; // Le déplacement est sécurisé
    }
    
    
    
    public void afficherCoordsPossibles(int xactu, int yactu) {
        ArrayList<coordonnee> coords = casesPossibles(xactu, yactu);
    
        System.out.println("Coordonnées possibles pour le Roi :");
        for (coordonnee coord : coords) {
            System.out.println("X : " + coord.getX() + ", Y : " + coord.getY());
        }
    }
    /*
    public boolean Roque(int x, int y)
    {
        // TODO: créer la fonction roque si on a le temps

        return true;
    }*/
}

