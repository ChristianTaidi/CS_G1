package cs.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

import cs.spaceinvaders.entity.Enemy;

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

        //for para probar distintas direcciones
            for(int l = 1; l<=2;l++){
                enemy.setEnemyMoving(l);
                if(l==1){
                    expectedDIR = 2;
                }else{
                    expectedDIR = 1;
                }
                enemy.enemyCicle();
                outputDIR = enemy.getEnemyMoving();
                assertEquals(expectedDIR, outputDIR, delta);
            }

    }
}