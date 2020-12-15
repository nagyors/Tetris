package Game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomShapeTest {
    CustomShape shape;

    @Before
    public void setUp(){
        shape = new CustomShape(new int[][]{{1,1,1}}, 5, "Vonal");
    }

    @Test
    public void getNameTest() {
        Assert.assertEquals("Vonal",shape.getName());
    }

    @Test
    public void getColorIntTest() {
        Assert.assertEquals(5,shape.getColorInt());
    }

    @Test
    public void getCoordsTest(){
        int[][] temp = new int[][]{{1,1,1}};

        Assert.assertEquals(temp,shape.getCoords());
    }
}