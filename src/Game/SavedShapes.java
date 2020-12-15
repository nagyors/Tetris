package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SavedShapes extends JPanel implements ActionListener {
    private final Menu menu;                                  //A használt Menu
    private final JPanel shapePanel, controlPanel;            //A felhasznált panelek. Az első az elem megjelenítésére szolgál, a második a felhasználói felület.
    private JButton[][] buttons = new JButton[3][4];          //A button tömb amire az elem ki lesz rajzolva
    private JComboBox namesCombo;                             //A nevekhez tartozó combobox

    private ArrayList<CustomShape> shapes;                    //Az elmentett elemeket tárolja
    private final AddShape addShape = new AddShape();         //Segítségével szerezhetők meg az elmentett elemek

    /**
     * A SavedShapes osztály konstruktora, beállítja a paraméterként kapott menüt az osztály menüjének.
     * Beállítja a panel alap beállításait, létrehoz egy button tömböt amire majd késöbb kirajzolja az elemeket.
     * Lekéri az elmentett saját elemeket és a neveit betölt a comboboxba, hogy a felhasználó tudjon elemet választani.
     * Létrehoz két gombot, egyet a törlésnek és egyet a visszalépésnek.
     * @param menu
     */
    public SavedShapes(Menu menu){
        this.menu = menu;
        this.setSize(450,600);
        this.setLayout(new BorderLayout());
        menu.getJMenuBar().setVisible(false);

        shapes = addShape.getShapes();

        GridLayout gridLayout=new GridLayout(3,4);
        shapePanel = new JPanel();
        shapePanel.setLayout(gridLayout);
        this.add(shapePanel,BorderLayout.CENTER);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                buttons[i][j] = new JButton();
                shapePanel.add(buttons[i][j]);
            }
        }

        controlPanel = new JPanel();

        ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < shapes.size(); i++)
            names.add(shapes.get(i).getName());

        String[] s = new String[names.size()];
        s = names.toArray(s);

        namesCombo = new JComboBox(s);
        namesCombo.addActionListener(this);
        controlPanel.add(namesCombo);

        JButton delete = new JButton("Delete");
        delete.addActionListener(new DeleteActionListener());
        controlPanel.add(delete, BorderLayout.EAST);

        JButton back = new JButton("Back");
        back.addActionListener(new BackActionListener());
        controlPanel.add(back, BorderLayout.EAST);


        this.add(controlPanel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    /**
     * Lekéri a comboboxban kiválasztott elemet és megkeresi az elmentett elemek között.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){
        String s = (String) namesCombo.getSelectedItem();
        if(shapes.size() != 0) {
            int index = 0;
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).getName().equals(s)) {
                    index = i;
                }
            }
            colorButton(index);
        }
    }

    /**
     * Frissíti az elemek listáját és az aktuális elem listáját betölti a comboboxba.
     */
    private void shapesRefresh(){
        shapes = addShape.getShapes();

        ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < shapes.size(); i++)
            names.add(shapes.get(i).getName());

        String[] s = new String[names.size()];
        s = names.toArray(s);

        namesCombo.removeAllItems();
        for(int i = 0; i < names.size(); i++){
            namesCombo.addItem(s[i]);
        }


    }

    /**
     *
     * @param index
     */
    private void colorButton(int index){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                if(shapes.get(index).getCoords()[i][j] == 1)
                    buttons[i][j].setBackground(shapes.get(index).getColor());
                else
                    buttons[i][j].setBackground(null);
            }
        }
        repaint();
    }

    /**
     * A Back gomb megnyomását kezeli le.
     * A MenuBar-t láthatóva teszi és az ő panelét pedig eltünteti.
     */
    public  class BackActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.getJMenuBar().setVisible(true);
            SavedShapes.this.setVisible(false);
        }
    }

    /**
     * A Delete gomb megnyomását kezeli le.
     * Lekéri a jelenleg kiválasztott elemet és kitörli az elemek listájából.
     */
    public  class DeleteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String s = (String) namesCombo.getSelectedItem();
            for(int i = 0; i < shapes.size(); i ++){
                if(shapes.get(i).getName().equals(s)){
                    shapes.remove(i);
                    addShape.afterRemoveSave(shapes);
                    shapesRefresh();
                }
            }
        }
    }
}
