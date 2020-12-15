package Game;

import java.awt.*;
import java.io.Serializable;

public class CustomShape implements Serializable {
    private final int coords[][];   //Az elem formáját tárolja egy mátrixban
    private final int color;        //Az elem színét tárolja
    private final String name;      //Az elem nevét tárolja

    /**
     * A CustomShape osztály konstruktora, beállítja a paramétek szerint az értékeket.
     * @param coords    Az tetszőleges elem formája.
     * @param color     A tetszőleges elem színe.
     * @param name      A tetszőleges elem neve.
     */
    public CustomShape(int coords[][], int color, String name){
        this.color=color;
        this.coords=coords;
        this.name=name;
    }

    /**
     * Az elem nevét adja vissza.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Az elem formájat adja vissza.
     * @return
     */
    public int[][] getCoords(){
        return coords;
    }

    /**
     * Az elem színét adja vissza egy számként.
     * @return
     */
    public int getColorInt(){
        return color;
    }

    /**
     * Az elem színet adja vissza színként.
     * @return
     */
    public Color getColor(){
        switch(color){
            case 1:
                return Color.red;
            case 2:
                return Color.orange;
            case 3:
                return Color.yellow;
            case 4:
                return Color.green;
            case 5:
                return Color.cyan;
            case 6:
                return Color.magenta;
            case 7:
                return Color.blue;
            default:
                return Color.gray;
        }
    }
}
