import java.awt.*;
import javax.swing.*;



public class Menu{

    static int boardType = 1;
    static int boardSize = 8;
    static boolean sizeBoardChange =true;
    static Color boardcolors[] = {new Color(173, 255, 47),new Color(173, 216, 230),new Color(222, 184, 135),new Color(34, 139, 34),new Color(25, 25, 142),new Color(139, 69, 19)};
    public static void main(String[] args) {
        // Fenêtre principale
        JFrame frame = new JFrame("Jeu d'échecs - Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);


        //Panneau principal avec CardLayout
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        

        // Ajouter les différentes pages
        JPanel homePage = createHomePage(mainPanel, cardLayout, frame);
        JPanel settingsPage = createSettingsPage(mainPanel, cardLayout);
        JPanel boardtypePage = createBoardTypePage(mainPanel, cardLayout);
        JPanel boardsizePage = createBoardSizePage(mainPanel, cardLayout);
        JPanel pieceConfigPage = createPieceConfigPage(mainPanel, cardLayout);
        JPanel gameOnlinePage = createGameOnlinePage(mainPanel, cardLayout);
        JPanel gamePage = createGamePage(mainPanel, cardLayout);
        //JPanel boardPage = createBoardPage(mainPanel, cardLayout, 8);

        mainPanel.add(homePage, "Accueil");
        mainPanel.add(settingsPage, "settings");
        mainPanel.add(boardsizePage, "boardSize");
        mainPanel.add(boardtypePage, "boardType");
        mainPanel.add(pieceConfigPage, "pieceConfig");
        mainPanel.add(gameOnlinePage, "gameOnline");
        mainPanel.add(gamePage, "game");

        // Afficher la page d'accueil
        cardLayout.show(mainPanel, "Accueil");


        // Ajouter le panneau principal à la fenêtre
        frame.add(mainPanel);
        frame.setVisible(true);
    }



    //Page de setting
    public static JPanel createSettingsPage(JPanel mainPanel, CardLayout cardLayout){
        //Principal
        JPanel settingsPanel = new JPanel();
        initPage(settingsPanel, "SETTINGS");
        
        // Panneau de boutons centré
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Rendre transparent pour voir le fond
        buttonPanel.setLayout(new GridLayout(3, 1, 100, 100));

        // Boutons avec style
        JButton btnSize = createStyledButton("Taille Echéquier");
        JButton btnType = createStyledButton("Type échequier");
        JButton btnModification = createStyledButton("Modification");
        JButton btnBack = createStyledButton("Retour");

        btnSize.addActionListener(e -> cardLayout.show(mainPanel, "boardSize"));
        btnModification.addActionListener(e -> cardLayout.show(mainPanel,"settings"));
        btnType.addActionListener(e -> cardLayout.show(mainPanel,"boardType"));
        btnBack.addActionListener(e -> cardLayout.show(mainPanel,"Accueil"));

        JPanel sup1 = new JPanel();
        sup1.setBackground(new Color(240, 217, 181));

        JPanel sup2 = new JPanel();
        sup2.setBackground(new Color(240, 217, 181));

        buttonPanel.add(btnSize);
        buttonPanel.add(btnType);
        buttonPanel.add(btnModification);

        settingsPanel.add(buttonPanel, BorderLayout.CENTER);
        settingsPanel.add(sup1, BorderLayout.EAST);
        settingsPanel.add(sup2, BorderLayout.WEST);
        settingsPanel.add(btnBack, BorderLayout.SOUTH);
         

        return settingsPanel;
    }

   
    //Page pour configurer les pieces ajouter

    public static JPanel createPieceConfigPage(JPanel mainPanel, CardLayout cardLayout) {
        JPanel pieceConfigPanel = new JPanel(new BorderLayout());
        initPage(pieceConfigPanel, "Configuration des Pièces");

        JPanel pieceOptions = new JPanel(new GridLayout(2, 1, 20, 20));
        pieceOptions.setOpaque(false);

        JButton btnAddPiece = createStyledButton("Ajouter une Nouvelle Pièce");
        btnAddPiece.addActionListener(e -> {
            String pieceName = JOptionPane.showInputDialog("Nom de la pièce :");
            if (pieceName != null && !pieceName.trim().isEmpty()) {
                // Intégrer avec le backend pour ajouter la pièce
                System.out.println("Nouvelle pièce ajoutée : " + pieceName);
            }
        });

        JButton btnModifyPiece = createStyledButton("Modifier une Pièce Existante");
        btnModifyPiece.addActionListener(e -> {
            String pieceName = JOptionPane.showInputDialog("Nom de la pièce à modifier :");
            if (pieceName != null && !pieceName.trim().isEmpty()) {
                // Intégrer avec le backend pour modifier la pièce
                System.out.println("Pièce modifiée : " + pieceName);
            }
        });

        JButton btnBack = createStyledButton("Retour");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));

        pieceOptions.add(btnAddPiece);
        pieceOptions.add(btnModifyPiece);

        pieceConfigPanel.add(pieceOptions, BorderLayout.CENTER);
        pieceConfigPanel.add(btnBack, BorderLayout.SOUTH);
        
        return pieceConfigPanel;
    }



    // Page pour choisir un type de plateau
    public static JPanel createBoardTypePage(JPanel mainPanel, CardLayout cardLayout){

        JPanel boardTypePanel = new JPanel();
        initPage(boardTypePanel, "Board Type");
        JPanel boards = new JPanel(new GridLayout(3, 3, 20, 20));
        boards.setBackground(new Color(240, 217, 181));

        JButton[][] choice = new JButton[3][3];
        
        Runnable updateBoardTypePanel = () -> {
            
            boards.removeAll();
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    JPanel sample = new JPanel(new BorderLayout());
                    sample.add(ChessGame.BoardInit(ChessGame.Pieces, boardSize, boardSize, boardcolors[j], boardcolors[j+3], 20));

                    int index = i*3+j+1;
                    choice[i][j] = createStyledButton(index+"");
                    choice[i][j].addActionListener(e -> {
                        boardType = index;
                        JOptionPane.showMessageDialog(null, "Type d'échiquier sélectionné : " + index);
                        boardType = index;
                    });
                    
                    sample.add(choice[i][j], BorderLayout.SOUTH);

                    boards.add(sample);
                }    
            }
            boards.revalidate();
            boards.repaint();
        };
    
        if(sizeBoardChange){
             updateBoardTypePanel.run();
             sizeBoardChange = false;
        }     
        boardTypePanel.add(boards);
        JButton btnBack = createStyledButton("Retour");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "settings"));
        boardTypePanel.add(btnBack, BorderLayout.SOUTH);
        boardTypePanel.putClientProperty("updateBoardTypePanel", updateBoardTypePanel);
        return boardTypePanel;
    }



    //Page pour definir la taille du plateau
    public static JPanel createBoardSizePage(JPanel mainPanel, CardLayout cardLayout){
       
        //Principal
        JPanel BoaderSizePanel = new JPanel(cardLayout);
        initPage(BoaderSizePanel, "Board Size");
    
        // Créer un JSlider pour ajuster la taille
        JSlider slider = new JSlider(4, 16, boardSize); // Plage de 4x4 à 16x16
        slider.setMajorTickSpacing(2); // Graduation principale tous les 2
        slider.setPaintTicks(true); // Afficher les graduations
        slider.setPaintLabels(true); // Afficher les labels
        slider.addChangeListener(e -> {
            // Mettre à jour le panneau des types d'échiquiers
            // L'indice 3 correspond à "BoardType"
            if ( boardSize != slider.getValue()) {
                boardSize = slider.getValue();
                JPanel typePanel = (JPanel) mainPanel.getComponent(3);
                Runnable setSize = (Runnable) typePanel.getClientProperty("updateBoardTypePanel");
                setSize.run();
            }
        });

        //Ajouter le jauge a la page
        BoaderSizePanel.add(slider);

        JButton bBack = createStyledButton("Retour");
        bBack.addActionListener(e -> cardLayout.show(mainPanel,"settings"));
        BoaderSizePanel.add(bBack, BorderLayout.SOUTH);

        return BoaderSizePanel;
    }



    //Page de jeu en ligne
    public static JPanel createGameOnlinePage(JPanel mainPanel, CardLayout cardLayout){
        JPanel gameOnlinePanel = new JPanel();

        //Initialiser la page
        initPage(gameOnlinePanel, "Online");

        JButton bBack = createStyledButton("Quitter");
        bBack.addActionListener(e -> cardLayout.show(mainPanel,"Accueil"));
        gameOnlinePanel.add(bBack, BorderLayout.SOUTH);


        return gameOnlinePanel;
    }



    //Page de jeu avec IA
    public static JPanel createGamePage(JPanel mainPanel, CardLayout cardLayout){
        JPanel gamePanel = new JPanel();

        //Initialiser la page
        initPage(gamePanel, "Game");

        JButton bBack = createStyledButton("Quitter");
        bBack.addActionListener(e -> cardLayout.show(mainPanel,"Accueil"));
        gamePanel.add(bBack, BorderLayout.SOUTH);
       
       
        return gamePanel;
    }


    //Page d'accueil
    public static JPanel createHomePage(JPanel mainPanel, CardLayout cardLayout, JFrame frame) {
        // Panneau principal avec fond personnalisé
        JPanel backgroundPanel = new JPanel();
        initPage(backgroundPanel, "CHESS");
        
        //Icon de fond
        backgroundPanel.add(backend("\u265B", new Color(240, 217, 181), 500)); 

        // Panneau de boutons ex-centré
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Rendre transparent pour voir le fond
        buttonPanel.setLayout(new GridLayout(4, 1));

        // Boutons avec style
        JButton btnPlay = createStyledButton("Lancer une Partie");
        JButton btnPlayOnline = createStyledButton("Multi Joueurs");
        JButton btnSettings = createStyledButton("Paramètres");
        JButton btnExit = createStyledButton("Quitter");

        btnPlay.addActionListener(e -> cardLayout.show(mainPanel, "game"));
        btnSettings.addActionListener(e -> cardLayout.show(mainPanel,"settings"));
        btnPlayOnline.addActionListener(e -> cardLayout.show(mainPanel,"gameOnline"));
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnPlay);
        buttonPanel.add(btnPlayOnline);
        buttonPanel.add(btnSettings);
        buttonPanel.add(btnExit);

        backgroundPanel.add(buttonPanel, BorderLayout.EAST);   
        return backgroundPanel; 
    }



    //image de garde
    public static JPanel backend(String text, Color color, int size){
        JPanel icon = new JPanel(new GridLayout(1,1));
        JLabel lab = new JLabel(text, JLabel.CENTER);
        lab.setFont(new Font( "ADLaM Display", Font.ROMAN_BASELINE, size));
        lab.setForeground(new Color(181, 136, 99));
        icon.setBackground(color);
        icon.add(lab);
        return icon;
    }



    // Méthode pour créer des boutons stylisés
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(181, 136, 99)); // Couleur marron foncé
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(240, 217, 181), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }


    //Initialise les page
    public static void initPage(JPanel page, String pageName){
        
        page.setLayout(new BorderLayout());
        page.setBorder(BorderFactory.createLineBorder(new Color(181, 136, 99), 5));
        page.setBackground(new Color(240, 217, 181));
       
       
        // Titre du jeu
        JLabel title = new JLabel(pageName, JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(new Color(181, 136, 99)); // Couleur du titre
        title.setBorder(BorderFactory.createLineBorder(new Color(181, 136, 99), 5));
        page.add(title, BorderLayout.NORTH);
    }
}
