import java.awt.*;
import javax.swing.*;;

public class ChessGame {
    
   static  String[][] Pieces = {
        {"\u2656","\u2658","\u2657","\u2655","\u2654","\u2657","\u2658","\u2656"},
        {"\u2659","\u2659","\u2659","\u2659","\u2659","\u2659","\u2659","\u2659"},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"\u265F","\u265F","\u265F","\u265F","\u265f","\u265F","\u265F","\u265F"},
        {"\u265C","\u265E","\u265D","\u265B","\u265A","\u265D","\u265E","\u265C"}
    };

   

    public static JPanel BoardInit(String[][] pieces, int rows, int cols, Color dark, Color light, int police ){


        JPanel Board = new JPanel(new GridLayout(rows, cols));
       

        

        for(int i = 0; i < rows; i++ ){
            for(int j = 0; j < cols; j++ ){
                JButton button = new JButton(Pieces[i][j]);
                button.setFont(new Font( "ADLaM Display", Font.ROMAN_BASELINE, police));
                button.setFocusPainted(false);
                if((i + j) % 2 == 0){
                    button.setBackground(dark);
                }
                else{
                    button.setBackground(light);
                }
                Board.add(button);
            }

        }

        return Board;
    }

    public static void main(String[] arg){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        JPanel Board = new JPanel(new GridLayout(8,8));
        Board = BoardInit(Pieces, 8, 8, new Color(173, 255, 47), new Color(34, 139, 34), 39);
        frame.add(Board);
        frame.setVisible(true);
    }

}

