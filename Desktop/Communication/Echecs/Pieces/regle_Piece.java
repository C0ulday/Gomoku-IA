import java.util.ArrayList;

public  interface regle_Piece {  // notre interface à repecter pour créer une pièce

    ArrayList<coordonnee> casesPossibles(int xactu, int yactu);

    void afficherCoordsPossibles(int xactu, int yactu);

    ArrayList<coordonnee> casesPrenable(int xactu, int yactu);


}  
 
