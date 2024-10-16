import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Gomoku {
    private static final int SIZE = 15; // Taille du plateau (15x15)
    private static final char EMPTY = '.';
    private static final char PLAYER1 = 'X'; // Représentation du joueur 1 (humain)
    private static final char PLAYER2 = 'O'; // Représentation du joueur 2 (ordinateur)
    private List<Character> board; // Plateau représenté comme une liste
    private char currentPlayer;
    private Random random;
    private String FICHIER_PLATEAUX = ""; // Fichier contenant les IDs et configurations des plateaux

    // Constructeur pour initialiser le jeu
    public Gomoku() {
        board = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            board.add(EMPTY);
        }
        currentPlayer = PLAYER1; // Le joueur 1 commence
        random = new Random();
    }

    // Affiche le plateau de jeu
    public void printBoard() {
        System.out.print("   ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i < 10 ? i + "  " : i + " ");
        }
        System.out.println();

        for (int row = 0; row < SIZE; row++) {
            String formattedNumber = String.format("%02d", row);
            System.out.print(formattedNumber + " ");
            for (int col = 0; col < SIZE; col++) {
                System.out.print(board.get(row * SIZE + col) + "  ");
            }
            System.out.println();
        }
        int id = getIDBoard(board);
        System.out.println("ID: " + "["+id+"]");

        if (!isDansFichier(id)) {
            ajouterPlateauDansFichier(id, getEvaluation(), board);
            System.out.println("Nouveau plateau ajouté avec un ID " + id);
        } else {
            System.out.println("ID existant !");
        }
    }

    // Permet au joueur actuel de faire un mouvement
    public void makeMove(int row, int col) {
        int index = row * SIZE + col;
        if (board.get(index) == EMPTY) {
            board.set(index, currentPlayer);
            if (checkWin(row, col)) {
                printBoard();
                System.out.println("Le joueur " + currentPlayer + " a gagné !");
                System.exit(0);
            }
            // Changer de joueur
            currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
        } else {
            System.out.println("La case est déjà occupée. Veuillez réessayer.");
        }
    }

    // Permet au joueur 2 (ordinateur) de jouer un coup aléatoire
    // public void makeRandomMove() {
    //     List<Integer> emptyPositions = new ArrayList<>();
    //     for (int i = 0; i < SIZE * SIZE; i++) {
    //         if (board.get(i) == EMPTY) {
    //             emptyPositions.add(i);
    //         }
    //     }
    //     if (!emptyPositions.isEmpty()) {
    //         int randomIndex = emptyPositions.get(random.nextInt(emptyPositions.size()));
    //         int row = randomIndex / SIZE;
    //         int col = randomIndex % SIZE;
    //         makeMove(row, col);
    //     }
    // }

    // Vérifie si le mouvement courant entraîne une victoire
    private boolean checkWin(int row, int col) {
        return (checkDirection(row, col, 1, 0) // Vérifie l'horizontale
                || checkDirection(row, col, 0, 1) // Vérifie la verticale
                || checkDirection(row, col, 1, 1) // Vérifie la diagonale descendante
                || checkDirection(row, col, 1, -1)); // Vérifie la diagonale montante
    }

    // Vérifie une direction pour les conditions de victoire
    private boolean checkDirection(int row, int col, int dRow, int dCol) {
        int count = 1; // Compte le pion actuel
        count += countStones(row, col, dRow, dCol); // Compte les pions dans une direction
        count += countStones(row, col, -dRow, -dCol); // Compte les pions dans l'autre direction
        return count >= 5; // Victoire si 5 pions alignés
    }

    // Compte les pions dans une direction donnée
    private int countStones(int row, int col, int dRow, int dCol) {
        int count = 0;
        char player = currentPlayer;
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }



    // Démarre le jeu avec interaction utilisateur
    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printBoard();
            System.out.println("C'est le tour du joueur " + currentPlayer);
            if (currentPlayer == PLAYER1) {
                try {
                    System.out.print("Entrez la ligne: ");
                    int row = scanner.nextInt();
                    System.out.print("Entrez la colonne: ");
                    int col = scanner.nextInt();
                    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                        makeMove(row, col);
                        System.out.println("Évaluation du plateau : " + getEvaluation());
                    } else {
                        System.out.println("Coordonnées invalides. Les valeurs doivent être entre 0 et " + (SIZE - 1));
                    }
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer des entiers.");
                    scanner.next(); // Consomme l'entrée incorrecte
                }
            } else {
                DefendreAttaque(3); // Le joueur 2 (ordinateur) joue
                System.out.println("Évaluation du plateau : " + getEvaluation());
            }
        }
    }
    // Fonction d'évaluation de l'état du plateau
    public int getEvaluation() {
        int score = 0;
        int ID = getIDBoard(board);

        if (!isDansFichier(ID)) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    char cell = board.get(row * SIZE + col);
                    if (cell == EMPTY) {
                        continue; // On ne considère pas les cases vides
                    }
                    if (cell == PLAYER2) {
                        score += evaluatePositionAndDirection(row, col);
                    }
                    if (cell == PLAYER1) {
                        score -= evaluatePositionAndDirection(row, col);
                    }
                }
            }
            
        } else {
            score = getScore(ID);
        }
        return score;
    }

    // Évalue une position pour un joueur dans toutes les directions
    private int evaluatePositionAndDirection(int row, int col) {
        int score = 0;
        score += evaluateDirection(row, col, 1, 0); // Horizontale
        score += evaluateDirection(row, col, 0, 1); // Verticale
        score += evaluateDirection(row, col, 1, 1); // Diagonale montante
        score += evaluateDirection(row, col, 1, -1); // Diagonale descendante
        return score;
    }

    // Évalue une direction donnée en comptant les pions et les ouvertures potentielles
    private int evaluateDirection(int row, int col, int dRow, int dCol) {
        int count = countStones(row, col, dRow, dCol);
        int openings = getPotentialOpenings(row, col, dRow, dCol);
        return (count * 2) + openings;
    }

    // Compte les ouvertures potentielles dans une direction donnée
    private int getPotentialOpenings(int row, int col, int dRow, int dCol) {
        int r = row + dRow;
        int c = col + dCol;
        return (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == EMPTY) ? 1 : 0;
    }

    // Permet au joueur 2 (ordinateur) de jouer un coup avec l'algorithme AlphaBeta
    public void makeAlphaBetaMove(int profondeurMaximale) {

        int meilleurCoup = -1;
        int meilleureValeur = Integer.MIN_VALUE;
        List<Integer> coupsPossibles = getCoupsPossibles();

        System.out.println("cherche le move");
        for (int coup : coupsPossibles) {
            board.set(coup, PLAYER2);
            int valeur = AlphaBeta(profondeurMaximale - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            board.set(coup, EMPTY);
    
            if (valeur > meilleureValeur) {
                meilleureValeur = valeur;
                meilleurCoup = coup;
            }
        }
    
        if (meilleurCoup != -1) {
            int row = meilleurCoup / SIZE;
            int col = meilleurCoup % SIZE;
            makeMove(row, col);
        }
        System.out.println("Trouvé le move");
    }
    // Implémentation de l'algorithme AlphaBeta
    private int AlphaBeta(int profondeur, boolean maximizingPlayer, int alpha, int beta) {
        if (profondeur == 0 || isGameOver()) {
            return getEvaluation();
        }
    
        List<Integer> coups = getCoupsPossibles();
    
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int coup : coups) {
                board.set(coup, PLAYER2);
                int eval = AlphaBeta(profondeur - 1, false, alpha, beta);
                board.set(coup, EMPTY);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int coup : coups) {
                board.set(coup, PLAYER1);
                int eval = AlphaBeta(profondeur - 1, true, alpha, beta);
                board.set(coup, EMPTY);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }
    /**********`isGameOver()`********
    //Ajout d'une condition d'arrêt pour éviter une récursion infinie.
    //vérifie si le jeu est terminé (victoire ou match nul).*/
    private boolean isGameOver() {
        // Vérifiez si le jeu est terminé (victoire ou match nul)
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (board.get(i) != EMPTY) {
                int row = i / SIZE;
                int col = i % SIZE;
                if (checkWin(row, col)) {
                    return true;
                }
            }
        }
        return !board.contains(EMPTY); // Match nul si le plateau est plein
    }
    /*********`getCoupsPossibles()` **********
    but : pour éviter de parcourir tout le plateau à chaque itération.
    retourne une liste des coups possibles (cases vides) pour optimiser la recherche.*/
    private List<Integer> getCoupsPossibles() {
        List<Integer> coups = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (board.get(i) == EMPTY) {
                coups.add(i);
            }
        }
        return coups;
    }
    
    // Stratégie de défense améliorée de l'ordinateur pour bloquer les menaces de l'adversaire
public void DefendreAttaque(int profondeur) {
    // Cherche les coups de défense critique (victoire imminente de l'adversaire)
    System.out.println("Avant trouver coup");
    int defenseMove = trouverCoupDefenseCritique();
    
    if (defenseMove != -1) {
        int row = defenseMove / SIZE;
        int col = defenseMove % SIZE;
        makeMove(row, col); // Bloque la victoire de l'adversaire
        System.out.println("L'IA a bloqué une victoire potentielle !");
    } else {
        // Si aucune défense critique nécessaire, chercher des menaces
        
        defenseMove = trouverMenace();
        
        if (defenseMove != -1) {
            int row = defenseMove / SIZE;
            int col = defenseMove % SIZE;
            System.out.println("Coup défendu : row " + row + " col " + col);
            makeMove(row, col); // Bloque la menace avant qu'elle ne devienne critique
            System.out.println("L'IA a défendu contre une menace !");
        } else {
            // Si aucune menace détectée, jouer un coup aléatoire
            FICHIER_PLATEAUX = "plateau"+profondeur+".txt";
            makeAlphaBetaMove(profondeur);
        }
    }
}

// Recherche un coup de défense critique (victoire imminente de l'adversaire)
private int trouverCoupDefenseCritique() {
    for (int i = 0; i < SIZE * SIZE; i++) {
        if (board.get(i) == EMPTY) {
            board.set(i, PLAYER1); // Simule un coup de l'adversaire (joueur humain)
            if (checkWin(i / SIZE, i % SIZE)) {
                board.set(i, EMPTY); // Annule la simulation
                System.out.println("Coup trouvé : " + i);
                return i; // Coup de défense trouvé
            }
            board.set(i, EMPTY); // Annule la simulation
        }
    }
    return -1; // Aucun coup de défense critique trouvé
}

// Recherche des menaces potentielles (3 ou 4 pions alignés pour l'adversaire)
private int trouverMenace() {
    for (int i = 0; i < SIZE * SIZE; i++) {
        if (board.get(i) == EMPTY) {
            // Simuler un coup de l'adversaire
            board.set(i, PLAYER1);
            boolean menace = MenaceExiste(i / SIZE, i % SIZE);
            board.set(i, EMPTY); // Annuler la simulation

            // Si une menace est détectée (alignement de 3 ou 4 pions)
            if (menace) {
                return i;
            }
        }
    }
    return -1; // Aucune menace détectée
}

// Évalue la menace pour un coup potentiel (vérifie les alignements adverses)
private boolean MenaceExiste(int row, int col) {
    int[] directions = {-1, 0, 1};

    // Vérifie les alignements dans toutes les directions (horizontale, verticale, diagonale)
    for (int dRow : directions) {
        for (int dCol : directions) {
            if (dRow == 0 && dCol == 0) continue;

            int alignement = compterAlignement(row, col, dRow, dCol);
            if (alignement >= 3) {
                return true;  // Si l'adversaire a 3 pions alignés
            }
        }
    }
    return false;
}

// Compte le nombre de pions alignés pour l'adversaire dans une direction donnée
private int compterAlignement(int row, int col, int dRow, int dCol) {
    int count = 0;
    char adversaire = PLAYER1;

    // Compter les pions adverses dans une direction (dRow, dCol)
    int r = row + dRow;
    int c = col + dCol;
    while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == adversaire) {
        count++;
        r += dRow;
        c += dCol;
    }

    // Compter les pions dans la direction opposée (-dRow, -dCol)
    r = row - dRow;
    c = col - dCol;
    while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == adversaire) {
        count++;
        r -= dRow;
        c -= dCol;
    }

    return count;  // Retourne le nombre total de pions alignés
}


    // Calcule un ID unique pour le plateau actuel
    public int getIDBoard(List<Character> board) {
        return board.hashCode();
    }

    // Vérifie si l'ID existe déjà dans le fichier
    public boolean isDansFichier(int id) {
        File file = new File(FICHIER_PLATEAUX);
        
        // Crée le fichier s'il n'existe pas
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Le fichier " + FICHIER_PLATEAUX + " a été créé.");
            } catch (IOException e) {
                System.out.println("Erreur lors de la création du fichier " + FICHIER_PLATEAUX);
                return false;
            }
        }
    
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                int idLu = scanner.nextInt();
                scanner.nextInt(); // Sauter l'évaluation
                if (idLu == id) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier " + FICHIER_PLATEAUX + " est introuvable.");
        }
        
        return false;
    }
    

    // Ajoute un plateau et son évaluation dans le fichier
    public void ajouterPlateauDansFichier(int id, int evaluation, List<Character> board) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_PLATEAUX, true));
            writer.write(id + " " + evaluation);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans le fichier " + FICHIER_PLATEAUX);
        }
    }

    // Récupère l'évaluation d'un plateau à partir du fichier
    public int getScore(int id) {
        try {
            Scanner scanner = new Scanner(new File(FICHIER_PLATEAUX));
            while (scanner.hasNextInt()) {
                int idLu = scanner.nextInt();
                int scoreLu = scanner.nextInt();
                if (idLu == id) {
                    scanner.close();
                    return scoreLu;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier " + FICHIER_PLATEAUX + " est introuvable.");
        }
        return 0;
    }

    public static void main(String[] args) {
        Gomoku game = new Gomoku();
        game.playGame();
    }
}
