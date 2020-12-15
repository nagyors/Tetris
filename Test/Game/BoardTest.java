package Game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    Board board;
    Menu menu = new Menu();
    Window window = new Window(15,20,menu);

    @Before
    public void setUp(){
        board = new Board(15,20,window);
    }

    @Test
    public void levelGrowTest() {
        for(int i = 0; i < 10; i++)
            board.levelGrow();
        Assert.assertEquals(2,board.getLevel());
    }

    @Test
    public void getBoardWidthTest() {
        int width = board.getBoardWidth();
        Assert.assertEquals(15,width);
    }

    @Test
    public void getBoardHeigtTest() {
        int height = board.getBoardHeight();
        Assert.assertEquals(20,height);
    }

    @Test
    public void getBlockSizeTest(){
        int blockSize = board.getBlockSize();
        Assert.assertEquals(30,blockSize);
    }

    @Test
    public void getBoardTest(){
        int[][] temp = new int[board.getBoardHeight()][board.getBoardWidth()];
        for(int i = 0; i < board.getBoardHeight(); i++)
            for(int j = 0; j < board.getBoardWidth(); j++)
                temp[i][j]=0;

        Assert.assertEquals(temp,board.getBoard());
    }
}