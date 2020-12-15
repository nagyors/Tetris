package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shape {
    private BufferedImage block;    //Az elem blokkjainak a képe
    private int[][] coords;         //Az elem formáját tároló mátrix
    private Board board;            //A játékmezőt tároló mátrix
    private int deltaX=0;           //X irányba való mozgatás. 1 jobbra, -1 balra
    private int x,y;                //Az elem helye a játékmezőn
    private int color;              //Az elem színéért felelős

    private int normalSpeed = 600, speedDown = 50, currentSpeed;    //Lefele eső sebességek
    private long time,lastTime;                                     //A lefele eső sebesség beállításában segítenek

    private boolean collision=false;    //Nézi, hogy az elem elért-e egy másik elemet vagy a pálya szélét
    private boolean moveX=true;         //igaz, hogyha X irányba lehet mozogni

    /**
     * A Shape osztály konstruktora.
     * Beállítja az alapbeállításokat a paraméteben kapott adatok szerint.
     * @param block     Az elem blokkjainak a képe.
     * @param coords    Az elem formája.
     * @param board     A játéktér állapota.
     * @param color     Az elem színe
     */
    public Shape(BufferedImage block,int[][] coords, Board board,int color){
        this.block=block;
        this.board=board;
        this.coords=coords;
        this.color=color;

        time=0;
        lastTime=System.currentTimeMillis();
        currentSpeed=normalSpeed;

        x = board.getBoardWidth()/2 - 2;
        y = 0;
    }

    /**
     * Frissíti az elemet, megnézi, hogy ütközött-e és hogy megtelt-e egy sor.
     * A gombnyomások szerint mozgatja az elemet jobbra vagy balra.
     * Az aktuális sebességel mozgatja az elemet lefele.
     */
    public void update(){
        time += System.currentTimeMillis() - lastTime;
        lastTime=System.currentTimeMillis();

        if(collision){
            for(int row=0;row<coords.length;row++)
                for(int col=0;col<coords[row].length;col++)
                    if(coords[row][col] != 0)
                        board.getBoard()[y+row][x+col] = color;


            checkLine();
            board.setCurrentShape();
        }

        if(!(x + deltaX + coords[0].length > board.getBoardWidth()) && !(x + deltaX < 0)){
            for(int row=0;row<coords.length;row++)
                for(int col=0;col<coords[row].length;col++)
                    if(coords[row][col] != 0)
                        if(board.getBoard()[y+row][x+col+deltaX] != 0)
                            moveX=false;
            if(moveX)
                x += deltaX;
        }


        if(!(y + coords.length + 1 > board.getBoardHeight())){
            for(int row=0;row<coords.length;row++)
                for(int col=0;col<coords[row].length;col++)
                    if(coords[row][col] != 0)
                        if(board.getBoard()[y+row+1][x+col] != 0)
                            collision=true;

            currentSpeed -= board.getLevel()*board.getLevel();
            if(time > currentSpeed){
                y++;
                time=0;
            }
        }
        else
            collision=true;

        deltaX=0;
        moveX=true;
    }

    /**
     * Visszaadja az elem blokkjainak a képét.
     * @return
     */
    public BufferedImage getBlock() {
        return block;
    }

    /**
     * Visszaadja az elem formájate egy tömbben.
     * @return
     */
    public int[][] getCoords() {
        return coords;
    }

    /**
     * Visszaaadja az elem színét.
     * @return
     */
    public int getColor(){
        return color;
    }

    /**
     * Forgatja az elemet, ha ez nem okoz átfedést más elemekkel a pályán.
     */
    public void rotate(){
        if(collision)
            return;

        int[][] rotated=null;
        rotated=getTransposeMatrix(coords);
        rotated=getReverseMatrix(rotated);

        if(x + rotated[0].length > board.getBoardWidth() || y + rotated.length > board.getBoardHeight())
            return;

        for(int row=0;row<rotated.length;row++)
            for(int col=0;col<rotated[0].length;col++)
                if(board.getBoard()[y+row][x+col] != 0)
                    return;

        coords = rotated;
    }

    /**
     * Megnézi, hogy van-e sor amik teljesen be vannak telve,
     * ha vannak, akkor ezekt törli és növeli a pontszámot.
     */
    private void checkLine(){
        int height=board.getBoard().length - 1;
        int width=board.getBoard()[0].length;
        int multiplier=1;

        for(int i=height;i>0;i--){
            int count=0;

            for(int j=0;j<width;j++){
                if(board.getBoard()[i][j] != 0)
                    count++;

                board.getBoard()[height][j] = board.getBoard()[i][j];
            }

            if(count < width)
                height--;
            else {
                board.scoreIncrease(100 * multiplier);
                multiplier++;
                board.levelGrow();
            }
        }
    }

    /**
     * Egy mátrix transzponáltját adja vissza.
     * @param matrix    A mátrix aminek a transzponáltját akarjuk.
     * @return          A transzponált mátrix.
     */
    private int[][] getTransposeMatrix(int[][] matrix){
        int[][] newMatrix = new int[matrix[0].length][matrix.length];

        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
                newMatrix[j][i]=matrix[i][j];
            }
        }

        return newMatrix;
    }

    /**
     * Tükröz egy mátrixot közepe szerint.
     * @param matrix    A mátrix amit tükrözni akarunk.
     * @return          A tükrözött mátrix.
     */
    private int[][] getReverseMatrix(int[][] matrix){
        int middle = matrix.length/2;

        for(int i=0;i<middle;i++){
            int[] temp=matrix[i];
            matrix[i]=matrix[matrix.length-i-1];
            matrix[matrix.length-i-1]=temp;
        }
        return matrix;
    }

    /**
     * Kirajzolja az elemet a Graphics osztály segítségével.
     * @param g
     */
    public void render(Graphics g){
        for(int i=0;i<coords.length;i++)
            for(int j=0;j<coords[i].length;j++)
                if(coords[i][j]!=0)
                    g.drawImage(block, j*board.getBlockSize() + x*board.getBlockSize(),
                            i*board.getBlockSize() + y*board.getBlockSize(),null);
    }

    /**
     * Beállítja a deltaX-et, az értéket ami eldönt, hogy jobbra vagy balra akarjuk mozgatni az elemet.
     * @param newDelta
     */
    public void setDeltaX(int newDelta){
        this.deltaX=newDelta;
    }

    /**
     * A sebességet átállítja gyorsabbra. (Ha meg lett nyomva a lefele gomb)
     */
    public void speedDown(){
        currentSpeed=speedDown;
    }

    /**
     * Visszaállítja a sebességet a normális sebességre.
     */
    public void normalSpeed(){
        currentSpeed=normalSpeed;
    }
}
