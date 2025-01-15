import java.util.ArrayList;

public class TestPlateau
{
    public static void main(String[] args) 
    {
        Plateau plateau = new Plateau(8); // Crée un plateau de 8x8
        // Crée un Cavalier
        //Cavalier cavalierBlanc = new Cavalier(9, 7, 9, 7, "BLANC", plateau);
        
        Roi roi = new Roi(4, 4, 4, 4, "BLANC", plateau);
        Tour tour = new Tour(7,4,7,4,"NOIR",plateau);
        Cavalier cav = new Cavalier(7,6,7,6,"NOIR",plateau);
        Pion pion = new Pion(5,5, 5, 5, "NOIR", plateau);
        plateau.placerPiece(roi, 4, 4);
        plateau.placerPiece(tour, 7, 4);
        plateau.placerPiece(cav, 7, 6);
        plateau.placerPiece(pion, 5, 4);
        roi.afficherCoordsPossibles(4, 4);
        cav.afficherCoordsPossibles(7, 6);
        pion.afficherCoordsPossibles(5, 4);
        tour.afficherCoordsPossibles(7, 4);
        //System.out.println("Coordonnées jouables initiales Roi  : " + roi.casesPrenable(4,4));
        //System.out.println("Coordonnées jouables initiales cava : " + cav.casesPrenable(7,6));

        // Remplir le plateau avec les pièces des deux joueurs
        // Vérifie si le roi est en échec
        if (regleJeuEchec.Echec(plateau,"BLANC")) {
            System.err.println("Le Roi est en échec.");
        } else {
            System.err.println("Le Roi est en sécurité.");
        }

        plateau.remplirPlateau();
        System.out.println("\n");
        // Afficher le plateau
        plateau.afficherPlateau();
    }
}

        //plateau.deplacementPiece(0,0,4,3);

        //plateau.afficherPlateau();
        
        
        /* 
        Plateau plateau = new Plateau(8); // Crée un plateau de 8x8
        // Crée un Cavalier
        //Cavalier cavalierBlanc = new Cavalier(9, 7, 9, 7, "BLANC", plateau);

        for (int i = 0; i < 8; i++) {
            plateau.ajouterPieceNoire(new Piece("pion", 1, i,1,i, "NOIR",plateau));
        }
        plateau.ajouterPieceNoire(new Piece("tour", 0, 0,0,0, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("tour", 0, 7,0,7, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("cavalier", 0, 1,0,1, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("cavalier", 0, 6,0,6, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("fou", 0, 2,0,2, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("fou", 0, 5,0,5, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("dame", 0, 3,0,3, "NOIR",plateau));
        plateau.ajouterPieceNoire(new Piece("roi", 0, 4,0,4, "NOIR",plateau));

        // Ajouter les pièces blanches
        for (int i = 0; i < 8; i++) {
            plateau.ajouterPieceBlanche(new Piece("Pion", 6, i,6,i, "BLANC",plateau));
        }
        plateau.ajouterPieceBlanche(new Piece("Tour", 7, 0,7,0, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Tour", 7, 7,7,7, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Cavalier", 7, 1,7,1, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Cavalier", 7, 6,7,6, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Fou", 7, 2,7,2, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Fou", 7, 5,7,5, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Cavalier", 7, 3,7,3, "BLANC",plateau));
        plateau.ajouterPieceBlanche(new Piece("Roi", 7, 4,7,4, "BLANC",plateau));

        // Remplir le plateau avec les pièces des deux joueurs
        plateau.remplirPlateau();

        // Afficher le plateau
        plateau.afficherPlateau();


        plateau.deplacementPiece(0,0,4,3);

        plateau.afficherPlateau();

        */

/*
// Teste un déplacement valide
        plateau.deplacementPiece(3, 2, 5, 3); // Déplacement valide pour le Cavalier
        System.out.println("Position actuelle Cavalier après déplacement valide : (" + cavalierBlanc.getPositionX() + ", " + cavalierBlanc.getPositionY() + ")");

        // Teste un déplacement non valide
        plateau.deplacementPiece(5, 3, 6, 6); // Déplacement invalide pour le Cavalier
        System.out.println("Position actuelle Cavalier après déplacement invalide : (" + cavalierBlanc.getPositionX() + ", " + cavalierBlanc.getPositionY() + ")"); 


        // Test d'une case vide
        Piece caseVide = plateau.getPiece(3, 2); // Case initiale du Cavalier, désormais vide
        System.out.println("Ancienne case (3, 2) : " + (caseVide != null ? caseVide.getName() : "Vide"));

        */

/*
// Place le Cavalier sur le plateau
        plateau.placerPiece(cavalierBlanc, 9, 7);
        plateau.placerPiece(roi, 3, 3);
        // Affiche la position initiale
        System.out.println("Position initiale Cavalier : (" + cavalierBlanc.getPositionX() + ", " + cavalierBlanc.getPositionY() + ")");
        System.out.println("Coordonnées jouables initiales : " + cavalierBlanc.casesPossiblesJouable(3, 2));

        // Vérification des cases jouables après déplacement
        System.out.println("Coordonnées jouables après déplacement : " + cavalierBlanc.casesPossiblesJouable(cavalierBlanc.getPositionX(), cavalierBlanc.getPositionY()));

        // Test d'une prise de pièce
        Piece pionNoir = new Piece("PION", 1, 1, 1, 1, "NOIR", plateau);
        Piece pionBlanc = new Piece("PION", 1, 3, 1, 3, "BLANC", plateau);
        plateau.placerPiece(pionNoir, 1, 1);
        System.out.println("Coordonnées jouables après déplacement pion noir: " + pionNoir.getPositionX() +" "+ pionNoir.getPositionY());

        plateau.placerPiece(pionBlanc, 1, 3);
        System.out.println("Coordonnées jouables après déplacement pion blanc: " + pionBlanc.getPositionX() + " "+ pionBlanc.getPositionY());

        //plateau.deplacementPiece(5, 3, 6, 1);
        System.out.println("Coordonnées jouables après déplacement  Cavalier: " + cavalierBlanc.casesPossiblesJouable(cavalierBlanc.getPositionX(), cavalierBlanc.getPositionY()));

        System.out.println("Coordonnées jouables initiales Tour : " + roi.casesPossiblesJouable(3, 3));
*/

/*

        Roi roi = new Roi(4, 4, 4, 4, "BLANC", plateau);
        plateau.placerPiece(roi, 4, 4);
        System.out.println("Coordonnées jouables initiales roi : " + roi.casesPossiblesJouable(4, 4));
        plateau.deplacementPiece(4, 4, 4, 7);
        Piece caseVide = plateau.getPiece(4, 4); // Case initiale du Cavalier, désormais vide
        System.out.println("case : " + (caseVide != null ? caseVide.getName() : "Vide"));   

*/