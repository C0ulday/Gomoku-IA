import java.util.ArrayList;

public class regleJeuEchec {

    public static boolean Echec(Plateau plateau,String couleur)
    {
        ArrayList<Piece> pieces = plateau.getPlateauPiece();
        int xRoi,yRoi;

        for (Piece roi : pieces) {
            // Vérifie si la pièce est le roi de la couleur couleur
            if (roi.getName().equals("ROI") && roi.getCouleur().equals(couleur)) 
            {
                // On récupère ses coordonnées
                xRoi = roi.getPositionX();
                yRoi = roi.getPositionY();
                
                // pour chaque pièce adverse au roi
                for (Piece piece : pieces) 
                {
                    if (piece instanceof regle_Piece && !piece.getCouleur().equals(couleur) ) {
                        ArrayList<coordonnee> casesAdverses = ((regle_Piece) piece).casesPossibles(piece.getPositionX(),piece.getPositionY());
        
                        // Vérifie si la position cible est attaquée
                        for (coordonnee coordAdverse : casesAdverses) {
                            if (coordAdverse.getX() == xRoi && coordAdverse.getY() == yRoi) {
                                return true; // Le déplacement met le roi en échec
                            }
                        }
                    }
                
                    // Cas particulier pour les pions (les pions attaquent en diagonale)
                    if (piece.getName().equals("PION")) 
                    {
                        int direction = piece.getCouleur().equals("BLANC") ? -1 : 1; // Sens d'attaque du pion
                        int pionX = piece.getPositionX();
                        int pionY = piece.getPositionY();
        
                        if ((pionX + direction == xRoi && pionY + 1 == yRoi) ||
                            (pionX + direction == xRoi && pionY - 1 == yRoi)) {
                            return true; // Le pion attaque la case cible
                        }
                    }
                }
            }
        }
        return false;
    }
    public static boolean Pat(Plateau plateau,String couleur)
    {
        ArrayList<Piece> pieces = plateau.getPlateauPiece();
        int xRoi,yRoi;

        for (Piece roi : pieces) {
            // Vérifie si la pièce est le roi de la couleur couleur
            if (roi.getName().equals("ROI") && roi.getCouleur().equals(couleur)) 
            {
                // On récupère ses coordonnées
                xRoi = roi.getPositionX();
                yRoi = roi.getPositionY();
                
                // pour chaque pièce adverse au roi
                for (Piece piece : pieces) 
                {
                    if (piece instanceof regle_Piece && piece.getCouleur().equals(couleur) ) {
                        ArrayList<coordonnee> casesAdverses = ((regle_Piece) piece).casesPossibles(piece.getPositionX(),piece.getPositionY());
        
                        // Vérifie si la position cible est attaquée
                        for (coordonnee coordAdverse : casesAdverses) {
                            if (coordAdverse.getX() == xRoi && coordAdverse.getY() == yRoi) {
                                return true; // Le déplacement met le roi en échec
                            }
                        }
                    }
                
                    // Cas particulier pour les pions (les pions attaquent en diagonale)
                    if (piece.getName().equals("PION")) 
                    {
                        int direction = piece.getCouleur().equals("BLANC") ? -1 : 1; // Sens d'attaque du pion
                        int pionX = piece.getPositionX();
                        int pionY = piece.getPositionY();
        
                        if ((pionX + direction == xRoi && pionY + 1 == yRoi) ||
                            (pionX + direction == xRoi && pionY - 1 == yRoi)) {
                            return true; // Le pion attaque la case cible
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean Mat(Plateau plateau,String couleur)
    {
        ArrayList<Piece> pieces = plateau.getPlateauPiece();
        int xRoi,yRoi;

        for (Piece roi : pieces) {
            // Vérifie si la pièce est le roi de la couleur couleur
            if (roi.getName().equals("ROI") && roi.getCouleur().equals(couleur)) 
            {
                // On récupère ses coordonnées
                xRoi = roi.getPositionX();
                yRoi = roi.getPositionY();
                

                
            }
        }
        return false;
    }

}