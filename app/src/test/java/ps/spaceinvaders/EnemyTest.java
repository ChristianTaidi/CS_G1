package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnemyTest {
    Context context;
    Bitmap a1;
    Bitmap a2;
    Bitmap a3;
    Bitmap a4;
    int screenX;
    int screenY;
    int row;
    int column;
    Enemy enemy;

    @Before
    public void setUp() throws Exception {
        context = null;
        a1 = null;
        a2 = null;
        a3 = null;
        a4 = null;
        screenX = 75;
        screenY = 500;
        row = 7;
        column = 5;
        enemy = new Enemy(context,row,column,screenX,screenY,a1,a2,a3,a4);
    }

    @Test
    public void enemyCicle() {
        double delta = 0.1;
        int expectedDIR;
        int outputDIR;
        float expected;
        float output;

        //for para probar distintos valores
        for(int x = 0; x<=100;x++){
            enemy.setX(x);
            for(int l = 1; l<=2;l++){
                enemy.setEnemyMoving(l);
                if(l==1){
                    expectedDIR = 2;
                    expected = x + 10;
                }else{
                    expectedDIR = 1;
                    expected = x - 10;
                }
                outputDIR = enemy.getEnemyMoving();
                output = enemy.getX();
                assertEquals(expected, output, delta);
                assertEquals(expectedDIR, outputDIR, delta);
            }
        }
    }
}