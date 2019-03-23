package cs.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

import cs.spaceinvaders.entity.SpaceShip;

import static org.junit.Assert.*;

public class InvadersGameViewTest {

    boolean isReloading;
    SpaceShip spaceShip;
    Context context;
    int screenX;
    int screenY;
    Bitmap b;


    @Before
    public void setUp() {
        isReloading = false;
        context = null;
        b = null;
        screenY = 500;
        screenX = 75;
        spaceShip = new SpaceShip(context, screenX, screenY, b);
    }

    @Test
    public void shoots() {
        int count = 0;
        long timerIni = System.currentTimeMillis();
        long timerEnd = 0;
        boolean standardShot = false;
        boolean specialShot = false;

        while (timerEnd-timerIni < 2000) {
            count++;
            if (count < 5)
                standardShot = true;
            else
                specialShot = true;

            timerEnd = System.currentTimeMillis();
            assertEquals((standardShot), (count < 5));
            assertEquals((specialShot), (count == 5));

            if (specialShot)
                count=0;
            spaceShip.addShootsCount();
            standardShot = false;
            specialShot = false;
        }
    }

}

