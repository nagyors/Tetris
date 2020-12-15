package Game;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Toplist implements Serializable {
    private ArrayList<Integer> scores;  //Ide menti a fájlból kiír pontokat

    /**
     * A Toplist osztály konstruktora.
     * Létrehoz egy input változót, amibe betölti a scores.dat fájlt, amiben a toplistás pontszámok vannak.
     */
    public Toplist(){
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(new FileInputStream("res/scores.dat"));
            scores = (ArrayList<Integer>) input.readObject();
        } catch (IOException e) {
            scores = new ArrayList<Integer>(10);
        }catch (ClassNotFoundException e) {
            System.err.println("Nem jo a toplist fajl.");
            System.exit(1);
        }
    }

    /**
     * A paraméterként kapott pontszámot hozzáadja a toplistához ha az benne van a legjobb tízben.
     * Kiírja a fájlba a listát.
     * @param score A betöltendő pontszám.
     */
    public void add(int score){
        if(scores.size() == 0){
            scores.add(score);
        }
        else{
            int index=0;
            for(int i = 0; i < scores.size(); i++){
                index=i;
                if(scores.get(i).compareTo(score) > 0){
                    if(i == scores.size() - 1)
                        index++;
                    continue;
                }
                else
                    break;
            }
            if(index < 10)
                scores.add(index, score);
        }

        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/scores.dat"));
            output.writeObject(scores);
            output.close();
        } catch (IOException e) {
            System.err.println("Nem tudott irni a fajlba/megnyitni a fajlt.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * A toplista elemeit írja ki a képernyőre egy MessageDialog-ban.
     * Ha nincs még benne elem, akkor tudatja a felhasználóval.
     */
    public void display(){
        if(scores.size() > 0){
            String output="";
            for(int i = 0; i < scores.size(); i++)
                output += (i + 1) + ") " + scores.get(i).toString() + "\n";
            JOptionPane.showMessageDialog(null, output);
        }
        else
            JOptionPane.showMessageDialog(null, "No scores to display");
    }
}
