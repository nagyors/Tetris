package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMap extends JPanel {
    private final Menu menu;                    //A használt menu
    private JComboBox heightCombo, widthCombo;  //A magasság és szélesség kiválasztásához használt comboboxok
    private final int maxHeight = 30;           //Maximum magasság
    private final int maxWidth = 30;            //Maximum szélesség
    private int boardWidth = 10;                //A játéktér szélessége
    private int boardHeight = 20;               //A játéktér magassága

    /**
     * Az EditMap osztály konstruktora, beállítja menünek a paraméterként kapott menüt.
     * Beállítja a panel alap beállításait és létrehoz rá két comboboxot, egyet a magasságnak és egyet a szélességnek.
     * Létrehoz három gombot, egyet a comboboxok állásának az elmentésére,
     * egyet az alapértelmezett mező beállítására(20 x 10) és egyet a visszalépésre.
     * @param menu
     */
    public EditMap(Menu menu){
        this.menu=menu;
        this.setSize(450,600);
        this.setLayout(new BorderLayout());
        menu.getJMenuBar().setVisible(false);

        JPanel contentPane = new JPanel();

        JLabel labelHeight = new JLabel();
        labelHeight.setText("Height of the map:");
        contentPane.add(labelHeight);

        Object[] height = new Object[maxHeight+1-10];
        for(int i = 10; i <= maxHeight; i++)
            height[i-10] = i;

        heightCombo = new JComboBox(height);
        contentPane.add(heightCombo);

        JLabel labelWidth = new JLabel();
        labelWidth.setText("Width of the map:");
        contentPane.add(labelWidth);

        Object[] width = new Object[maxWidth+1-10];
        for(int i = 10; i <= maxWidth; i++)
            width[i-10] = i;

        widthCombo = new JComboBox(width);
        contentPane.add(widthCombo);

        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ConfirmActionListener());
        contentPane.add(confirm);

        JButton defaultSettings = new JButton("Default");
        defaultSettings.addActionListener(new DefaultActionListener());
        contentPane.add(defaultSettings);

        JButton back = new JButton("Back");
        back.addActionListener(new BackActionListener());
        contentPane.add(back);

        this.add(contentPane);

        this.setVisible(true);
    }

    /**
     * A Back gomb megnyomását kezeli le.
     * A MenuBar-t láthatóva teszi és az ő panelét pedig eltünteti.
     */
    public  class BackActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.getJMenuBar().setVisible(true);
            EditMap.this.setVisible(false);
        }
    }

    /**
     * A Confirm gomb megnyomását kezeli le.
     * Lekéri a comboboxok értékeit és beállítja azokat magásságnak és szélességnek.
     * Kiírja a felhasználónak, hogy el lettek mentve az értéket.
     */
    public  class ConfirmActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int h = (Integer) heightCombo.getSelectedItem();
            int w = (Integer) widthCombo.getSelectedItem();

            boardWidth = w;
            boardHeight = h;

            menu.setBoardHeight(h);
            menu.setBoardWidth(w);

            JOptionPane.showMessageDialog(null, "The parameters have been saved!");
        }
    }

    /**
     * A Default gomb megnyomását kezeli le.
     * A magasságot 20-ra és a szélességet 10-re állítja.
     */
    public  class DefaultActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boardHeight = 20;
            boardWidth = 10;

            menu.setBoardHeight(boardWidth);
            menu.setBoardWidth(boardHeight);
        }
    }
}
