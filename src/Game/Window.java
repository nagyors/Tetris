package Game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public static int height,width; //A frame magassága és szélessége pixelben
    public Board board;             //A használandó játéktér
    private Menu menu;              //A használt menu

    /**
     * A Window osztály konstruktora. Beállítja az alap beállításokat.
     * Beállítja a framek listenerjeit.
     * @param boardWidth    A játékmező szélessége.
     * @param boardHeight   A játékmező magassága.
     * @param menu          A menü.
     */
    public Window(int boardWidth, int boardHeight, Menu menu){
        this.setTitle("Tetris");
        this.menu = menu;
        board = new Board(boardWidth, boardHeight, this);
        height = boardHeight*30 + 50;
        width = boardWidth*30 + 160;

        this.setSize(width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.add(board);
        this.addKeyListener(board);
        this.addMouseListener(board);
        this.addMouseMotionListener(board);

        this.setVisible(true);
    }

    /**
     * Visszalép, ezt a framet és a rajta található elemeket törli és a menü láthatóvá teszi.
     */
    public void goBack(){
        menu.setVisible(true);
        this.dispose();
    }
}
