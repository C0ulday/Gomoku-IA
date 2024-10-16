import java.util.Random;
import java.util.Scanner;

public class IA extends Gomoku {

    private char PLAYER;

    public IA(int p1, int p2, int p3, int p4, int p5, int profondeur, char PLAYER) {
        super(p1, p2, p3, p4, p5, profondeur);
        this.PLAYER = PLAYER;
    }

    // Méthode pour lire les paramètres de l'IA
    private static int[] lireParametres(Scanner scanner) {
        int[] params = new int[5];

        System.out.print("Entrez le poids le plus faible p1 : ");
        params[0] = scanner.nextInt();
        System.out.print("Entrez le poids p2 : ");
        params[1] = scanner.nextInt();
        System.out.print("Entrez le poids p3 : ");
        params[2] = scanner.nextInt();
        System.out.print("Entrez le poids p4 : ");
        params[3] = scanner.nextInt();
        System.out.print("Entrez le poids le plus fort p5 : ");
        params[4] = scanner.nextInt();

        return params;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\t\t# Affrontement de deux IA de Gomoku #\t\t");

        // Lecture des paramètres pour la première IA
        System.out.println("Choisissez le nom de la première IA : ");
        char player1 = scanner.nextLine().charAt(0);
        int[] params1 = lireParametres(scanner);
        
        System.out.print("Entrez la profondeur souhaitée : ");
        int profondeur1 = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne


        IA ia1 = new IA(params1[0], params1[1], params1[2], params1[3], params1[4], profondeur1, player1);

        // Lecture des paramètres pour la deuxième IA
        System.out.println("Choisissez le nom de la deuxième IA : ");
        char player2 = scanner.nextLine().charAt(0);
        int[] params2 = lireParametres(scanner);
        
        System.out.print("Entrez la profondeur souhaitée : ");
        int profondeur2 = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne


        scanner.close();
        IA ia2 = new IA(params2[0], params2[1], params2[2], params2[3], params2[4], profondeur2, player2);

        // Optionnel : Afficher les informations des IA créées
        System.out.println("IA 1 : " + ia1.PLAYER + " (p1: " + params1[0] + ", p2: " + params1[1] + ", profondeur: " + profondeur1 + ")");
        System.out.println("IA 2 : " + ia2.PLAYER + " (p1: " + params2[0] + ", p2: " + params2[1] + ", profondeur: " + profondeur2 + ")");

        Random random = new Random();
        char currentPlayer = random.nextBoolean() ? player1 : player2;

        // Début du jeu
        while (true) { 
            if(currentPlayer == player1){
                ia1.printBoard();
                ia1.DefendreAttaque();
            } else {
                ia2.DefendreAttaque();
                ia2.printBoard();
            }
        }
        // Fermer le scanner
        
    }
}
