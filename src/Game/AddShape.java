package Game;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class AddShape {
    private ArrayList<CustomShape> shapes; //Ide menti a fájlból az elemeket

    /**
     * Az AddShape osztály konstruktora.
     * Létrehoz egy input változót, amibe betölti a shapes.dat fájlt, amiben CustomShapes típusú elemek vannak.
     * Ha még nincs ilyen fájl akkor létrehozza a tömböt amiben tárolja őket.
     */
    public AddShape(){
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(new FileInputStream("res/shapes.dat"));
            shapes = (ArrayList<CustomShape>) input.readObject();
        } catch (IOException e) {
            shapes = new ArrayList<CustomShape>(5);
        }catch (ClassNotFoundException e) {
            System.err.println("Nem jo a shapes fajl.");
            System.exit(1);
        }
    }

    /**
     * Hozzáadja a tömbhöz az elemet a paraméterként kapott elemek segítségével.
     * Az elem nevét egy InputDialog segítségével kéri be.
     * A fájba beleírja a tömböt, ha beletudja, és bezárja a fájl-t.
     * @param coords    Az elem formája.
     * @param color     Az elem színe.
     */
    public void add(int coords[][], int color){
        shapes.add(new CustomShape(coords, color,JOptionPane.showInputDialog("Name of your shape?")));

        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/shapes.dat"));
            output.writeObject(shapes);
            output.close();
        } catch (IOException e) {
            System.err.println("Nem tudott irni a fajlba/megnyitni a fajlt.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * A paraméterként kapott elemet kiírja a fájlba.
     * @param newShapes     Törlés után az új elem tömb.
     */
    public void afterRemoveSave(ArrayList<CustomShape> newShapes){
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/shapes.dat"));
            output.writeObject(newShapes);
            output.close();
        } catch (IOException e) {
            System.err.println("Nem tudott irni a fajlba/megnyitni a fajlt.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Az elmentett elemek tömbjét adja vissza.
     * @return
     */
    public ArrayList<CustomShape> getShapes(){
        return shapes;
    }

}
