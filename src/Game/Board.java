package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    private BufferedImage tiles, background, pause, back;       //A játékhoz használt képek
    private ArrayList<BufferedImage> blocks = new ArrayList<>();//Az elemek blokkjainak a képei
    private final int blockSize = 30;                           //Egy blokk mérete
    private int boardHeight, boardWidth;                        //A játékmező magassága és szélessége blokkokban
    private int[][] board;                                      //A játék mezőt tároló mátrix,
                                                                //egy elem értéke 0 ha nincs ott semmi, egy színnek megfelelő szám, ha van ott elem

    private ArrayList<Shape> shapes = new ArrayList<>();        //Az elemeket tároló tömb
    private Shape currentShape, nextShape;                      //Az aktuális és a következő elem
    private Toplist toplist = new Toplist();;                   //A toplistába való íráshoz használt elem
    private AddShape addShape = new AddShape();                 //A felhasználó által csinált elemeket szerzi meg

    private boolean gameOver=true;                              //Tárolja, hogy a játék még megy-e
    private boolean gamePaused=false;                           //Tárolja, hogy a játék le van-e állítva a játék
    private boolean firstKeyPressed=false;                      //Tárolja, hogy már volt-e gomb megnyomva
    private int level = 1;                                      //Változó a szinteknek
    private int rowCounter = 0;                                 //Számolja, a törölt sorok számát

    private Timer timer;                                        //A játék folyamatos frissítéséért felelős timer
    private final int FPS=60;                                   //framek másodpercenként
    private final int delay=1000/FPS;                           //a delay a timerhez

    private int score = 0;                                      //A pontszámot tárolja

    private ArrayList<CustomShape> customShapes;                //A felhasználó által készített elemeket tárolja

    private int mouseX, mouseY;                                 //A kruzor X és Y koordinátája
    private boolean leftClick = false;                          //Klikkelést nézi
    private Rectangle pauseBounds, backBounds;                  //A pause és back gombok helyét tárolja
    private Window window;                                      //A játék JFrame-je

    private Timer buttonLapse = new Timer(300, new ActionListener(){ //Gombnyomás utáni várakozás

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }});

    /**
     * A Board osztály konstruktra. Beállítja az alap beállításokat és a menüben változtatott beállításokat is.
     * Betölti a szükséges képeket.
     * Feltölti az elemek tömböt az alap elemekkel és a felhasználó által készített elemekkel.
     * @param boardWidth    A játékmező szélessége.
     * @param boardHeight   A játékmező magassága.
     * @param window        A frame amin van a játék.
     */
    public Board(int boardWidth, int boardHeight, Window window){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.window = window;
        this.setLayout(null);
        this.setSize(boardWidth*blockSize + 150, boardHeight*blockSize);

        mouseX = 0;
        mouseY = 0;

        tiles = ImageLoader.loadImage("/tiles.png");

        background = ImageLoader.loadImage("/boardBackground.jpg");

        pause = ImageLoader.loadImage("/pause.png");

        back = ImageLoader.loadImage("/back.png");

        timer=new Timer(delay, new GameLooper());

        board = new int[boardHeight][boardWidth];
        setBoardZero();

        pauseBounds = new Rectangle(boardWidth*blockSize + 20, boardHeight*blockSize-100,
                                    pause.getWidth(), pause.getHeight() + pause.getHeight()/2);

        backBounds = new Rectangle(boardWidth*blockSize + 20, boardHeight*blockSize-150,
                                    back.getWidth(), pause.getHeight() + back.getHeight()/2);

        for(int i=0;i<7;i++) {
            blocks.add(tiles.getSubimage(blockSize * i, 0, blockSize, blockSize));
        }

        defaultShapes();
        getCustomShapes();
    }


    /**
     * A Graphics osztály segítségével rajzol a képernyőre.
     * Kirajzolja a pályát(a négyzet hálókat), a megfelelő méret szerint, a pályán szereplő elemeket a megfelelő színnel,
     * a következő elemet, az eddig elért pontszámot és a szintet amelyiken van a játékos.
     * Kirajzol még két képet, egy pause és egy back képet, amik a játék megállításáért és a visszalépésért felelősek.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(background, 0,0,null);

        if(!gameOver) {
            currentShape.render(g);
            for(int row = 0; row < nextShape.getCoords().length; row ++)
                for(int col = 0; col < nextShape.getCoords()[0].length; col ++)
                    if(nextShape.getCoords()[row][col] != 0)
                        g.drawImage(nextShape.getBlock(), col*30 + (boardWidth*blockSize)+ 20, row*30 + 50, null);
        }

        for(int row=0;row<board.length;row++)
            for(int col=0;col<board[row].length;col++)
                if(board[row][col] != 0)
                    g.drawImage(blocks.get(board[row][col]-1),col*blockSize,row*blockSize,null);

        g.setColor(Color.red);
        for(int i = 0; i <= boardHeight; i++){
            g.drawLine(0, i*blockSize, boardWidth*blockSize, i*blockSize);
        }
        for(int i = 0; i <= boardWidth; i++){
            g.drawLine(i*blockSize, 0, i*blockSize, boardHeight*blockSize);
        }

        g.drawImage(pause, pauseBounds.x, pauseBounds.y, null);
        g.drawImage(back, backBounds.x, backBounds.y, null);

        g.drawString("NEXT SHAPE:", (boardWidth*blockSize) + 20, 40);

        g.drawString("SCORE:", Window.width - 125, Window.height/2 - 30);
        g.drawString(score+"", Window.width - 125, Window.height/2);

        g.drawString("LEVEL:", Window.width - 125, Window.height/2 + 30);
        g.drawString(level+"", Window.width - 125, Window.height/2 + 60);

        if(gamePaused)
        {
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, boardWidth*blockSize/2 - 100, boardHeight*blockSize/2);
        }
    }

    /**
     * Létrehozza az alap elemeket amik a játékban vannak és hozzáadja az elemek tömbhöz.
     */
    private void defaultShapes(){
        shapes.add(new Shape(blocks.get(0),new int[][]{
                {1,1,1,1},
        },this,1));    //I

        shapes.add(new Shape(blocks.get(1),new int[][]{
                {1,1,0},
                {0,1,1}
        },this,2));    //Z
        shapes.add(new Shape(blocks.get(2),new int[][]{
                {1,1,1},
                {0,0,1}
        },this,3));    //J
        shapes.add(new Shape(blocks.get(3),new int[][]{
                {1,1,1},
                {1,0,0}
        },this,4));    //L
        shapes.add(new Shape(blocks.get(4),new int[][]{
                {0,1,0},
                {1,1,1}
        },this,5));    //T
        shapes.add(new Shape(blocks.get(5),new int[][]{
                {1,1},
                {1,1}
        },this,6));    //O
        shapes.add(new Shape(blocks.get(6),new int[][]{
                {0,1,1},
                {1,1,0}
        },this,7));    //S
    }

    /**
     * Frissíti a játékállást, megnézi, hogy lejárt-e a játék vagy megvan-t állítva.
     * Lekezeli a pause és a back képekre való kattintást.
     */
    public void update(){
        if(pauseBounds.contains(mouseX,mouseY) && !buttonLapse.isRunning() && leftClick) {
            buttonLapse.start();
            gamePaused = !gamePaused;
        }

        if(backBounds.contains(mouseX,mouseY) && leftClick){
            window.goBack();
        }

        if(gamePaused || gameOver)
            return;

        currentShape.update();

        if(gameOver) {
            timer.stop();
            firstKeyPressed=false;
            toplist.add(score);
            JOptionPane.showMessageDialog(null, "Game over!");
        }
    }

    /**
     * A játák elindításáért felelős.
     * A board tömböt 0-ra állítja, ezzel üres lesz a pálya és létrehozza az elemeket amivel indítani fog a játék.
     */
    private void startGame() {
        setBoardZero();
        setNextShape();
        setCurrentShape();
        gameOver=false;
        firstKeyPressed=true;
        score = 0;
        timer.start();
    }

    /**
     * A board tömb elemeit 0-ra állítja, így üres lesz a játéktér.
     */
    private void setBoardZero(){
        score = 0;

        for(int row = 0; row < board.length; row++)
        {
            for(int col = 0; col < board[row].length; col ++)
            {
                board[row][col] = 0;
            }
        }
        timer.stop();
    }

    /**
     * Betölti a felhasználó által készített elemekt a shapes.dat fájlból.
     * A tetszőleges elemeket hozzáadja az elem tömbhöz amit a játék használ.
     */
    private void getCustomShapes(){
        customShapes = addShape.getShapes();

        for(int i = 0; i < customShapes.size(); i++){
            CustomShape temp = customShapes.get(i);
            shapes.add(new Shape(blocks.get(temp.getColorInt() - 1), temp.getCoords(),
                    this, temp.getColorInt()));
        }
    }

    /**
     * Random beállítja a következő elemet az elem tömbből.
     */
    public void setNextShape(){
        int i = (int)(Math.random()*shapes.size());

        nextShape= new Shape(shapes.get(i).getBlock(),shapes.get(i).getCoords(), this, shapes.get(i).getColor());
    }

    /**
     * Beállítja az aktuális elemet a már létrehozott következő elemre. Random következő elemet generál.
     * Megnézi, hogy az akutális elemet eltudja-e helyezni a pályán, ha nem a gameOver-t igazra állítja.
     */
    public void setCurrentShape(){
        currentShape = nextShape;
        setNextShape();

        for(int row=0;row<currentShape.getCoords().length;row++)
            for(int col=0;col<currentShape.getCoords()[0].length;col++)
                if(currentShape.getCoords()[row][col] != 0)
                    if(board[row][col+3] != 0)
                        gameOver=true;
    }

    /**
     * Visszadja a táblán lévő blokkok méretét.
     * @return
     */
    public int getBlockSize(){
        return blockSize;
    }

    /**
     * Visszaadja a játékmező magasságát.
     * @return
     */
    public int getBoardHeight(){
        return  boardHeight;
    }

    /**
     * Visszaadja a játékmező szélességét.
     * @return
     */
    public int getBoardWidth(){
        return boardWidth;
    }

    /**
     * Visszaadja a játékmező mátrixát.
     * @return
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * Visszaadja a jelenlegi szintet.
     * @return
     */
    public int getLevel(){
        return level;
    }

    /**
     * A szintet növeli, hogyha már 5-nél több sor volt eltüntetve az elöző szintnövelés óta.
     */
    public void levelGrow(){
        rowCounter++;

        if(rowCounter > 5){
            level++;
            rowCounter=0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Lekezeli a kattintást.
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            leftClick = true;
    }

    /**
     * Lekezeli a kattintást.
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            leftClick = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Megszerzi az kurzor koordinátáit.
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * Megszerzi az kurzor koordinátáit.
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * A játék loopolásáért felelős timer, meghívja a frissítést és az újra festést.
     */
    class GameLooper implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            repaint();
        }

    }

    /**
     * Az a paraméter értéket adja hozzá a pontszámhoz.
     * @param a
     */
    public void scoreIncrease(int a){
        score += a;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * A gombnyomásokat kezeli le.
     * Mozgatja az aktuális elemet a megnyomott gomb szerint.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(firstKeyPressed) {
            if ((e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_A))
                currentShape.setDeltaX(-1);
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_D))
                currentShape.setDeltaX(1);
            if ((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S)) {
                scoreIncrease(1);
                currentShape.speedDown();
            }
            if ((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_W))
                currentShape.rotate();
        }
        else
            startGame();
    }

    /**
     * A gomb elengedését kezeli le.
     * Visszaállítja az elem sebességét normálisra.
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S))
            currentShape.normalSpeed();
    }
}
