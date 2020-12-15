package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Menu extends JFrame implements ActionListener{
    private ImageIcon background;   //A menü háttere
    private final JMenuBar menuBar;   //A MenuBar
    private final JMenu start, exit, toplist, shapes, boardEdit;  //A MenuBaron szereplő menük
    private final JMenuItem newShape, showShapes, boardParams, toplistItem;   //A menük almenüik, amik megjelennek, hogyha a menüre kattintanak
    private final JLabel backgroundPanel;   //Egy label aminek a képe a háttérkép lesz. Ezen lesznek a többi elemek
    private final Toplist leaderboard = new Toplist();  //Egy toplista
    private int boardWidth = 10, boardHeight = 20;  //A játékmező paraméterei

    /**
     * A Menu osztály konstruktora.
     * Beállítja az alap beállításokat, címet ad a frame-nek.
     * Betölti a háttérképet és létrehozza a menüt és a menü elemeit.
     */
    public Menu(){
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450 ,600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        try {
            background=new ImageIcon(ImageIO.read(Menu.class.getResource("/menuBg.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundPanel = new JLabel();
        backgroundPanel.setIcon(background);

        this.add(backgroundPanel);

        menuBar = new JMenuBar();
        start = new JMenu("Start");
        exit = new JMenu("Exit");
        toplist = new JMenu("Toplist");
        shapes = new JMenu("Shapes");
        boardEdit = new JMenu("Edit");
        newShape = new JMenuItem("New shape");
        showShapes = new JMenuItem("See the saved shapes");
        boardParams = new JMenuItem("Set the board's parameters");
        toplistItem = new JMenuItem("See the 10 best scores");

        start.addMenuListener(new StartMenuListener());
        exit.addMenuListener(new ExitMenuListener());
        newShape.addActionListener(this);
        showShapes.addActionListener(this);
        boardParams.addActionListener(this);
        toplistItem.addActionListener(this);

        menuBar.add(start);

        menuBar.add(toplist);
        toplist.add(toplistItem);

        menuBar.add(shapes);
        shapes.add(newShape);
        shapes.add(showShapes);

        menuBar.add(boardEdit);
        boardEdit.add(boardParams);
        menuBar.add(Box.createHorizontalGlue());


        menuBar.add(exit);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }

    public static void main(String[] args){
        new Menu();
    }

    /**
     * Beállítja a játékmező szélességét.
     * @param boardWidth
     */
    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    /**
     * Beállítja a játékmező magasságát.
     * @param boardHeight
     */
    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    /**
     * A MenuItemek megnyomását kezeli le és egy megfelelő osztályt hoz létre minden esethez.
     * A toplista esetén kiírja a toplista elemeit.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newShape){
            ShapeCreator shapeCreator = new ShapeCreator(Menu.this);
            backgroundPanel.add(shapeCreator);
            backgroundPanel.revalidate();
            backgroundPanel.repaint();
        }
        if(e.getSource() == showShapes){
            SavedShapes savedShapes = new SavedShapes(Menu.this);
            backgroundPanel.add(savedShapes);
            backgroundPanel.revalidate();
            backgroundPanel.repaint();
        }
        if(e.getSource() == boardParams){
            EditMap editMap = new EditMap(Menu.this);
            backgroundPanel.add(editMap);
            backgroundPanel.revalidate();
            backgroundPanel.repaint();
        }

        if(e.getSource() == toplistItem){
            leaderboard.display();
        }
    }

    /**
     * A Start menü megnyomását kezeli le.
     * Létrehoz egy új Window osztályt és a menüt nem láthatóvá allítja.
     */
    private class StartMenuListener implements MenuListener {

        @Override
        public void menuSelected(MenuEvent e) {
            new Window(boardWidth, boardHeight, Menu.this);
            Menu.this.setVisible(false);
        }

        @Override
        public void menuDeselected(MenuEvent e) {}

        @Override
        public void menuCanceled(MenuEvent e) {}
    }

    /**
     * Az Exit menü megnyomását kezeli le.
     * Kilép a programból.
     */
    private static class ExitMenuListener implements MenuListener {

        @Override
        public void menuSelected(MenuEvent e) {
            System.exit(0);
        }

        @Override
        public void menuDeselected(MenuEvent e) {}

        @Override
        public void menuCanceled(MenuEvent e) {}
    }
}
