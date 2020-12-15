package Game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class AddShapeTest {
    AddShape addShape;
    ArrayList<CustomShape> shapes1, shapesTest;

    @Before
    public void setUp(){
        addShape = new AddShape();
        shapes1 = new ArrayList<>();
        shapes1 = addShape.getShapes();
        shapesTest = shapes1;

        int[][] temp = new int[][]{{1,1,1,1},{0,0,1,0}};

        addShape.add(temp,5);
    }

    @Test
    public void addTest() {
        int[][] temp = new int[][]{{1,1,1}};

        addShape.add(temp,4);

        ArrayList<CustomShape> shapes2;
        shapes2 = addShape.getShapes();

        Assert.assertEquals(shapes1,shapes2);
    }

    @Test
    public void removeTest(){
        int[][] temp = new int[][]{{1,1,1,1},{0,0,1,0}};

        for(int i = 0; i < shapes1.size();i++){
            if(shapes1.get(i).getCoords() == temp)
                shapes1.remove(i);
        }

        addShape.afterRemoveSave(shapes1);

        Assert.assertEquals(shapes1,shapesTest);
    }
}