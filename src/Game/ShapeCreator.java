package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapeCreator extends JPanel implements ActionListener {
    private JButton[][] buttons=new JButton[3][4];  //Button tömb az elemek létrehozásához
    private final JComboBox colors;                 //Színeket tároló combobox
    private Color selectedColor;                    //Kiválasztott szín
    private int color=1;                            //Kiválasztott szín számként
    private int[][] selected = new int[3][4];       //Mátrix a kiválasztott gombok tárolásához
    private final Menu menu;                        //Használt menu
    private final AddShape addShape;                //Az elemek fájlba kiírásához kell

    /**
     * A ShapeCreator osztály konstruktora, beállítja menünek a paraméterként kapott menüt.
     * Beállítja a panel alap beállításait és létrehoz rá egy button tömböt és egy szín választó combobox-ot.
     * Létrehoz két button-t, egyet az elmentésnek és egyer a visszalépésnek.
     * @param menu
     */
    public ShapeCreator(Menu menu){
        this.menu = menu;
        this.setSize(450,600);
        this.setLayout(new BorderLayout());
        menu.getJMenuBar().setVisible(false);

        selectedColor = Color.red;
        addShape = new AddShape();

        GridLayout gridLayout=new GridLayout(3,4);
        JPanel makeShape = new JPanel();
        makeShape.setLayout(gridLayout);
        this.add(makeShape,BorderLayout.CENTER);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                buttons[i][j] = new JButton();
                buttons[i][j].setVisible(true);
                buttons[i][j].setActionCommand(i + " " + j);
                buttons[i][j].addActionListener(new ButtonActionListener());
                selected[i][j] = 0;
                makeShape.add(buttons[i][j]);
            }
        }


        JPanel controlPanel = new JPanel();

        String[] colorStrings = {"Red", "Orange", "Yellow", "Green", "Light blue", "Purple", "Dark blue"};
        colors = new JComboBox(colorStrings);
        colors.addActionListener(this);
        controlPanel.add(colors, BorderLayout.WEST);


        JButton add = new JButton("Add");
        add.addActionListener(new AddActionListener());
        controlPanel.add(add, BorderLayout.EAST);

        JButton back = new JButton("Back");
        back.addActionListener(new BackActionListener());
        controlPanel.add(back, BorderLayout.EAST);

        this.add(controlPanel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    /**
     * Megfelelő színre állítja a kiválasztott buttonöket és a repaint() segítségével láthatóvá teszi ezt.
     */
    private void buttonBackground(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                if(selected[i][j] == 1)
                    buttons[i][j].setBackground(selectedColor);
                else
                    buttons[i][j].setBackground(null);
            }
        }
        repaint();
    }

    /**
     * Az Add gomb megnyomását kezeli le. A létrehozott elemet hozzáadja a saját elemekhez
     * és a buttonök hátterét visszaállítja null-ra.
     */
    private class AddActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            addShape.add(selected, color);
            JOptionPane.showMessageDialog(null, "Your shape has been saved!");
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 4; j++){
                    selected[i][j]=0;
                }
            }
            buttonBackground();
        }
    }

    /**
     * A Back gomb megnyomását kezeli le és visszalép a menübe.
     * A MenuBar-t láthatóvá teszi és az elemszerkesztőt eltünteti.
     */
    private  class BackActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.getJMenuBar().setVisible(true);
            ShapeCreator.this.setVisible(false);
        }
    }

    /**
     * Az elemszerkesztőhöz szükséges buttonök megnyomását kezeli le.
     * Megszerzi a buttonökben tárolt koordinátákat
     * és a kiválasztott gombokért felelős selected tömb értékeit 1-re vagy 0-ra állítja.
     * Meghivjá a button színezéséért felelős függvényt.
     */
    private class ButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            int i = Character.getNumericValue(s.charAt(0));
            int j = Character.getNumericValue(s.charAt(2));

            if(selected[i][j] == 0)
                selected[i][j] = 1;
            else
                selected[i][j] = 0;
            buttonBackground();
        }
    }


    /**
     * A comboboxok értékeit kezeli le.
     * A kiválasztott elemet lekéri és aszerint állítja be a szín változókat.
     * Meghivjá a button színezéséért felelős függvényt.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = (String) colors.getSelectedItem();

        switch(s){
            case "Red":
                color = 1;
                selectedColor = Color.red;
                break;
            case "Orange":
                color = 2;
                selectedColor = Color.orange;
                break;
            case "Yellow":
                color = 2;
                selectedColor = Color.yellow;
                break;
            case "Green":
                color = 4;
                selectedColor = Color.green;
                break;
            case "Light blue":
                color = 5;
                selectedColor = Color.cyan;
                break;
            case "Purple":
                color = 6;
                selectedColor = Color.magenta;
                break;
            case "Dark blue":
                color = 7;
                selectedColor = Color.blue;
                break;
            default:
                System.out.println("Nem volt semmi kivalaszta.");
                break;
        }

        buttonBackground();
    }
}
