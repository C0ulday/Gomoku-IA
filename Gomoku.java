import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    private boolean jeuFini = false;
    private int Profondeur;
    private String fichier1,fichier2;
    private int[] poids1,poids2;
    // utilisation de caches pour etre plus rapide
    private HashMap<Integer, Integer> evaluationCache1 = new HashMap<>();
    private HashMap<Integer, Integer> evaluationCache2 = new HashMap<>();

    // Constructeur pour initialiser le jeu
    public Gomoku(int Profondeur,String fichier1,String fichier2,int[] poids1,int[] poids2) {

        this.Profondeur = Profondeur;
        this.fichier1 = fichier1;
        this.fichier2 = fichier2;

        this.poids1 = poids1;
        this.poids2 = poids2;
        
        board = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            board.add(EMPTY);
        }
        currentPlayer = PLAYER1; // Le joueur 1 commence
        random = new Random();
        evaluationCache1 = new HashMap<>();
        evaluationCache2 = new HashMap<>();
        chargerEvaluations(fichier1,evaluationCache1); // charge dans les fichiers
        chargerEvaluations(fichier2,evaluationCache2); // charge dans les fichiers
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

        if(currentPlayer == PLAYER1){
            if (!isDansFichier(id,evaluationCache1)) {
                ajouterPlateauDansFichier(fichier1,id, getEvaluation(poids1,evaluationCache1), board,evaluationCache1);
            } else {
                System.out.println("ID existant !");
            }
        }
        if (!isDansFichier(id,evaluationCache2)) {
            ajouterPlateauDansFichier(fichier2,id, getEvaluation(poids2,evaluationCache2), board,evaluationCache2);
        }
    }

    // Permet au joueur actuel de faire un mouvement
    public void makeMove(int row, int col) {
        int index = row * SIZE + col;
        if (board.get(index) == EMPTY) {
            board.set(index, currentPlayer);
            if (checkWin(row, col)) {
                    printBoard();
                    String nomFichier = "poids_gagnants.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
                        if (currentPlayer == PLAYER1) {
                            writer.write("Poids gagnants pour la profondeur " + Profondeur + ": " + Arrays.toString(poids1));
                        } else {
                            writer.write("Poids gagnants pour la profondeur " + Profondeur + ": " + Arrays.toString(poids2));
                        }
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Erreur lors de l'écriture dans le fichier: " + e.getMessage());
                    }
                    System.out.println("Le joueur" + currentPlayer + "a gagné !");
                    jeuFini = true; // Mettre à jour le statut de jeu
                    return; // Sortir de la méthode
                }
                // Changer de joueur
                currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
            } else {
                System.out.println("La case est déjà occupée. Veuillez réessayer.");
            }
    }
    // Vérifie si le mouvement courant entraîne une victoire
    private boolean checkWin(int row, int col) {
        // Arrête la recherche dès qu'une direction gagnante est trouvée
        return (checkDirection(row, col, 1, 0) || // Vérifie l'horizontale
                checkDirection(row, col, 0, 1) || // Vérifie la verticale
                checkDirection(row, col, 1, 1) || // Vérifie la diagonale montante
                checkDirection(row, col, 1, -1)); // Vérifie la diagonale descendante
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

    // Fonction d'évaluation de l'état du plateau
    
    public int getEvaluation(int[] poids,HashMap<Integer,Integer> evaluationCache) {
        int score = 0;
        int ID = getIDBoard(board);

        if (!isDansFichier(ID,evaluationCache)) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    char cell = board.get(row * SIZE + col);
                    if (cell == EMPTY) {
                        continue;
                    }
                    if (cell == PLAYER2) {
                        score += evaluatePositionAndDirection(row, col, PLAYER2,poids);
                    }
                    if (cell == PLAYER1) {
                        score -= evaluatePositionAndDirection(row, col, PLAYER1,poids);
                    }
                }
            }
        } else {
            score = getScore(ID,evaluationCache);
        }
        return score;
    }

    // Évalue une position pour un joueur dans toutes les directions
    private int evaluatePositionAndDirection(int row, int col, char player, int[] poids) {
        int score = 0;
        score += evaluateDirection(row, col, 1, 0, player,poids); // Horizontal
        score += evaluateDirection(row, col, 0, 1, player,poids); // Vertical
        score += evaluateDirection(row, col, 1, 1, player,poids); // Diagonal ascending
        score += evaluateDirection(row, col, 1, -1, player,poids); // Diagonal descending
        return score;
    }

    // Évalue une direction donnée en comptant les pions et les ouvertures potentielles
    private int evaluateDirection(int row, int col, int dRow, int dCol, char player, int[] poids) {
        int count = 1;
        int openEnds = 0;

        // Count in positive direction
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == EMPTY) {
            openEnds++;
        }

        // Count in negative direction
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }
        if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == EMPTY) {
            openEnds++;
        }

        // Evaluate the pattern
        if (count >= 5) return poids[5]; // Win
        if (count == 4 && openEnds == 2) return poids[4]; // Open four
        if (count == 4 && openEnds == 1) return poids[3]; // Closed four
        if (count == 3 && openEnds == 2) return poids[2]; // Open three
        if (count == 3 && openEnds == 1) return poids[1]; // Closed three
        if (count == 2 && openEnds == 2) return poids[0]; // Open two

        return (count * 10) + openEnds; 
    }
    // Permet au joueur 2 (ordinateur) de jouer un coup avec l'algorithme AlphaBeta
    public void makeAlphaBetaMove(int profondeurMaximale,int[] poids,HashMap<Integer, Integer> evaluationCache) {
        int meilleurCoup = -1;
        int meilleureValeur = Integer.MIN_VALUE;
        List<Integer> coupsPossibles = getCoupsPossiblesLimites();
    
        System.out.println("Recherche du meilleur coup...");
        for (int coup : coupsPossibles) {
            board.set(coup, PLAYER2);
            int valeur = AlphaBeta(profondeurMaximale - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0,poids,evaluationCache);
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
        System.out.println("Coup trouvé");
    }
    private List<Integer> getCoupsPossiblesLimites() {
        List<Integer> coups = new ArrayList<>();
        int minRow = SIZE, maxRow = 0, minCol = SIZE, maxCol = 0;
    
        // Trouver les limites du plateau occupé
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (board.get(i) != EMPTY) {
                int row = i / SIZE;
                int col = i % SIZE;
                minRow = Math.min(minRow, row);
                maxRow = Math.max(maxRow, row);
                minCol = Math.min(minCol, col);
                maxCol = Math.max(maxCol, col);
            }
        }
    
        // Élargir la zone de recherche
        minRow = Math.max(0, minRow - 2);
        maxRow = Math.min(SIZE - 1, maxRow + 2);
        minCol = Math.max(0, minCol - 2);
        maxCol = Math.min(SIZE - 1, maxCol + 2);
    
        // Ajouter les coups possibles dans cette zone
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                int index = row * SIZE + col;
                if (board.get(index) == EMPTY) {
                    coups.add(index);
                }
            }
        }
    
        // Si aucun coup n'est trouvé (début de partie), jouer au centre
        if (coups.isEmpty()) {
            coups.add(SIZE * SIZE / 2);
        }
    
        return coups;
    }
    // Implémentation de l'algorithme AlphaBeta
    private int AlphaBeta(int profondeur, boolean maximizingPlayer, int alpha, int beta, int profondeurActuelle,int[] poids,HashMap<Integer, Integer> evaluationCache) {
        if (profondeur == 0) {
            return getEvaluation(poids,evaluationCache) * (profondeurActuelle + 1);
        }

        List<Integer> coups = getCoupsPossiblesLimites();

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int coup : coups) {
                board.set(coup, PLAYER2);
                int eval = AlphaBeta(profondeur - 1, false, alpha, beta, profondeurActuelle + 1,poids,evaluationCache);
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
                int eval = AlphaBeta(profondeur - 1, true, alpha, beta, profondeurActuelle + 1,poids,evaluationCache);
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


    // Stratégie de défense améliorée de l'ordinateur pour bloquer les menaces de l'adversaire
    public void DefendreAttaque(int profondeur,int[] poids,HashMap<Integer, Integer> evaluationCache) {
        int defenseMoveScore = -1;
        int bestDefenseMove = -1;
        int attackMoveScore = -1;
        int bestAttackMove = -1;
    
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (board.get(i) == EMPTY) {
                // Check defense
                board.set(i, PLAYER1);
                int defenseScore = evaluatePositionAndDirection(i / SIZE, i % SIZE, PLAYER1,poids);
                if (defenseScore > defenseMoveScore) {
                    defenseMoveScore = defenseScore;
                    bestDefenseMove = i;
                }
                board.set(i, EMPTY);
    
                // Check attack
                board.set(i, PLAYER2);
                int attackScore = evaluatePositionAndDirection(i / SIZE, i % SIZE, PLAYER2,poids);
                if (attackScore > attackMoveScore) {
                    attackMoveScore = attackScore;
                    bestAttackMove = i;
                }
                board.set(i, EMPTY);
            }
        }
    
        if (attackMoveScore >= defenseMoveScore && attackMoveScore >= 1000) {
            makeMove(bestAttackMove / SIZE, bestAttackMove % SIZE);
            System.out.println("L'IA a joué un coup offensif !");
        } else if (defenseMoveScore >= 500) {
            makeMove(bestDefenseMove / SIZE, bestDefenseMove % SIZE);
            System.out.println("L'IA a bloqué une menace importante !");
        } else {
            makeAlphaBetaMove(profondeur,poids,evaluationCache);
        }
    }
   // Calcule un ID unique pour le plateau actuel
    public int getIDBoard(List<Character> board) {
        return board.hashCode();
    }
    //gestion des fichiers pour le memoire 
    // Méthode pour charger toutes les évaluations depuis le fichier dans la HashMap
    private void chargerEvaluations(String fichier,HashMap<Integer, Integer> evaluationCache) {

        File file = new File(fichier);
        if (!file.exists()) {
            try {
                // Crée le fichier
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Une erreur s'est produite lors de la création ou de l'écriture dans le fichier.");
                e.printStackTrace();
            }
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextInt()) {
                int idLu = scanner.nextInt();
                int scoreLu = scanner.nextInt();
                evaluationCache.put(idLu, scoreLu); // Charger dans la HashMap
            }
            System.out.println("Évaluations chargées depuis le fichier.");
        } catch (FileNotFoundException e) {
            System.out.println("Erreur lors du chargement des évaluations: " + e.getMessage());
        }
    }
    // Méthode mise à jour pour vérifier si l'évaluation existe déjà
    public boolean isDansFichier(int id,HashMap<Integer, Integer> evaluationCache) {
        return evaluationCache.containsKey(id); // Vérifie dans la HashMap
    }
    // Récupère l'évaluation d'un plateau à partir de la HashMap
    public int getScore(int id,HashMap<Integer, Integer> evaluationCache) {
        return evaluationCache.getOrDefault(id, 0); // Retourne 0 si l'ID n'existe pas
    }
    public void ajouterPlateauDansFichier(String fichier,int id, int evaluation, List<Character> board,HashMap<Integer, Integer> evaluationCache) {
        if (!evaluationCache.containsKey(id)) { // Si l'ID n'est pas déjà dans la HashMap
            evaluationCache.put(id, evaluation); // Ajouter dans la HashMap
        }

        // Sauvegarde périodique ou à la fin du jeu (par exemple, toutes les 10 évaluations)
        if (evaluationCache.size() % 10 == 0) {
            sauvegarderEvaluations(fichier,evaluationCache);
        }
    }
// Sauvegarde toutes les évaluations dans le fichier
    private void sauvegarderEvaluations(String fichier,HashMap<Integer, Integer> evaluationCache) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, true))) {
            for (int id : evaluationCache.keySet()) {
                writer.write(id + " " + evaluationCache.get(id));
                writer.newLine();
            }
            System.out.println("Évaluations sauvegardées dans " + fichier);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des évaluations : " + e.getMessage());
        }
    }


// Méthode pour faire jouer deux IA
    public void playIAvsIA() {

    
        printBoard();
        while (!jeuFini) {
            if (currentPlayer == PLAYER1) {
                System.out.println("IA 1 joue...");
                DefendreAttaque(Profondeur, poids1,evaluationCache1);
            } else {
                System.out.println("IA 2 joue...");
                DefendreAttaque(Profondeur, poids2,evaluationCache2); // IA 2 joue
            }
            printBoard();
        }
    }

    public static void main(String[] args) {

        // Définir des poids différents pour chaque combinaison
        int[][] poidsVariantes1 = {
            {20, 50, 600, 700, 800, 1000},
            {30, 60, 650, 750, 850, 900},
            {10, 40, 550, 650, 750, 1100},
            {25, 55, 620, 720, 820, 950},
            {15, 45, 580, 680, 770, 1050},
            {35, 65, 630, 730, 830, 920},
            {5, 30, 500, 600, 700, 1200},
            {40, 70, 670, 770, 870, 980},
            {18, 48, 590, 690, 780, 1100},
            {28, 58, 640, 740, 840, 940}
        };
        
        int[][] poidsVariantes2 = {
            {20, 50, 60, 70, 80, 1000},
            {30, 55, 65, 75, 85, 900},
            {10, 45, 55, 65, 75, 1100},
            {22, 52, 62, 72, 82, 950},
            {32, 57, 67, 77, 87, 850},
            {12, 42, 52, 62, 72, 1150},
            {25, 55, 65, 80, 90, 1000},
            {35, 60, 70, 85, 95, 920},
            {18, 46, 56, 66, 76, 1080},
            {27, 54, 64, 74, 84, 940}
        };
        
    
        // Définir la plage de profondeur à tester
        int[] profondeurs = {1,2,3,4,5}; // Ajustez les profondeurs selon vos besoins
        

        // Tester chaque combinaison de poids et de profondeur
        for (int i = 0; i < profondeurs.length; i++) {
            for (int j = 0; j < poidsVariantes1.length; j++) {

                String fichier1 = "eval/eval_ia1_" + profondeurs[i] + ".txt";
                String fichier2 = "eval/eval_ia2_" + profondeurs[i] + ".txt";

                // Créer une instance du jeu avec les poids et profondeur actuels
                Gomoku game = new Gomoku(profondeurs[i],fichier1, fichier2, poidsVariantes1[j], poidsVariantes2[j]);
                System.out.println("Lancement du jeu avec profondeur: " + profondeurs[i] +
                                   " et poids1: " + Arrays.toString(poidsVariantes1[j]) +
                                   " et poids2: " + Arrays.toString(poidsVariantes2[j]));
                game.playIAvsIA();
            }
        }
    }
}

