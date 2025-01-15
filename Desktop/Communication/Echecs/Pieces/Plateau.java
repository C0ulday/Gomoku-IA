import java.util.ArrayList;
import java.util.List;

public class Plateau {
    private List<Piece> plateau;
    private int SIZE;

    private List<Piece> piecesNoires; // Liste des pièces du joueur noir
    private List<Piece> piecesBlanches; // Liste des pièces du joueur blanc

    // Constructeur
    public Plateau(int SIZE) {
        this.SIZE = SIZE;
        plateau = new ArrayList<>(SIZE * SIZE);

        // Initialisation du plateau avec des cases vides
        for (int i = 0; i < SIZE * SIZE; i++) {
            plateau.add(new Piece("NULL", -1, -1,-1,-1, "NULL",this));
        }

        piecesNoires = new ArrayList<>();
        piecesBlanches = new ArrayList<>();
    }

    public void remplirPlateau() {
        // Ajouter les pièces noires
        for (Piece piece : piecesNoires) {
            if (estDansLesLimites(piece.getPositionXinit(), piece.getPositionYinit())) {
                plateau.set(piece.getPositionXinit() * SIZE + piece.getPositionYinit(), piece);
            }
        }

        // Ajouter les pièces blanches
        for (Piece piece : piecesBlanches) {
            if (estDansLesLimites(piece.getPositionXinit(), piece.getPositionYinit())) {
                plateau.set(piece.getPositionXinit() * SIZE + piece.getPositionYinit(), piece);
            }
        }
    }

    public void afficherPlateau() {
        // Affichage des numéros de colonnes
        System.out.print("  "); // Espace pour aligner avec la numérotation des lignes
        for (int y = 0; y < SIZE; y++) {
            System.out.print(y + " ");
        }
        System.out.println(); // Nouvelle ligne
    
        // Affichage du plateau avec numéros de lignes
        for (int x = 0; x < SIZE; x++) {
            System.out.print(x + " "); // Numéro de la ligne
            for (int y = 0; y < SIZE; y++) {
                Piece piece = plateau.get(x * SIZE + y);
                if ("NULL".equals(piece.getName())) {
                    System.out.print(". "); // Case vide
                } else {
                    char affichage = "BLANC".equals(piece.getCouleur()) 
                        ? piece.getName().toUpperCase().charAt(0) // Majuscule pour les pièces blanches
                        : piece.getName().toLowerCase().charAt(0); // Minuscule pour les pièces noires
                    System.out.print(affichage + " "); // Affichage de la pièce
                }
            }
            System.out.println(); // Nouvelle ligne après chaque rangée
        }
    }
    
    

    // Ajoute une pièce à la liste du joueur noir
    public void ajouterPieceNoire(Piece piece) {
        piecesNoires.add(piece);
    }

    // Ajoute une pièce à la liste du joueur blanc
    public void ajouterPieceBlanche(Piece piece) {
        piecesBlanches.add(piece);
    }

    public void viderCase(int x, int y) 
    {
        // Remplace la pièce à la position donnée par une pièce neutre ("NULL")
        plateau.set(x * SIZE + y, new Piece("NULL", -1, -1, -1, -1, "NULL", this));
    }
    
    public Piece getPiece(int x, int y) 
    {
        if (estDansLesLimites(x, y)) 
        {
            return plateau.get(x * SIZE + y);
        }
        return null; // Retourne null si les coordonnées sont en dehors des limites
    }
    /*
    jouer permet de jouer
    */
    public void jouerPiece()
    {
    }

    public boolean deplacementDansPlateau(int xactu, int yactu, int xnew, int ynew) {
        // Récupérer la pièce à déplacer

        if (!estDansLesLimites(xnew, ynew)) { // Vérifie si le déplacement est possible
            System.out.println("Déplacement interdit !");
            return false;
        }
        Piece piece  = getPiece(xactu, yactu);
        Piece piece2 = getPiece(xnew, ynew);
        // Récupère la pièce à déplacer
        
    
        if (piece == null || "NULL".equals(piece.getName())) {
            System.out.println("Aucune pièce à déplacer à cette position !");
            return false;
        }
    
        // Vérifier si une pièce est présente à la position actuelle
        if (piece == null || "NULL".equals(piece.getName())) {
            System.out.println("Aucune pièce à déplacer à cette position !");
            return false;
        }
        // Obtenir les cases possibles pour cette pièce
        ArrayList<coordonnee> casesPossibles = ((regle_Piece) piece).casesPossibles(xactu, yactu);
    
        // Vérifier si la case cible est autorisée
        boolean deplacementValide = false;
        for (coordonnee coord : casesPossibles) {
            if (coord.getX() == xnew && coord.getY() == ynew) {
                deplacementValide = true;
                break;
            }
        }
    
        if (!deplacementValide) {
            System.out.println("Déplacement interdit : La case cible n'est pas dans les mouvements possibles !");
            return false;
        }
    
        // Appeler l'ancienne méthode pour effectuer le déplacement

    
        System.out.println("Déplacement validé !");
        return true;
    }

    public void deplacementPiece(int xactu, int yactu, int xnew, int ynew) {
        
        if(deplacementDansPlateau(xactu,yactu,xnew,ynew))
        {
            Piece piece  = plateau.get(xactu * SIZE + yactu);
            Piece piece2 = plateau.get(xnew * SIZE + ynew);
            if( piece.getCouleur() != piece2.getCouleur() && !isCaseOccupee(xnew,ynew,"NULL") )
            {
                PrisePiece(xnew,ynew);
            }

            // Met à jour les coordonnées actuelles de la pièce
            piece.setPosition(xnew, ynew);
        
            // Place la pièce à la nouvelle position dans le plateau
            placerPiece(piece, xnew, ynew);
        
            // Vide l'ancienne case
            viderCase(xactu, yactu);


            System.out.println("Déplacement réussi !");
        }
    }
    

    public boolean estDansLesLimites(int x, int y) 
    {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public boolean isCaseOccupee(int x, int y, String couleur) 
    {
        Piece piece = plateau.get(x * SIZE + y);
        return piece != null && piece.getCouleur().equals(couleur); //NULL si on veut vérifier si une case est vide
    }

    public void placerPiece(Piece piece, int x, int y) 
    {
        if (estDansLesLimites(x,y))
        {
            plateau.set(x * SIZE + y, piece);
            System.out.println("Pièce placee avec succes");
        }
        else
        {
            System.out.println("coordonnées invalide");
        }
        
    }

    public void PrisePiece(int x, int y)
    {
        //TODO réaliser la fonction

        //Piece piece = getPiece(x,y);

    }

    public ArrayList<Piece> getPlateauPiece()
    {
        ArrayList<Piece> p = new ArrayList<Piece>();
        
        int x,y;

        for(int i=0; i<SIZE*SIZE;i++)
        {
            x = i / SIZE;
            y = i % SIZE;
            if(this.getPiece(x,y) != null)
            {
                p.add(this.getPiece(x, y));
            }
        }
        return p;
    }

    public int getSIZE() {

        return this.SIZE;
    }
}