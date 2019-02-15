package ps.spaceinvaders;

import android.graphics.Bitmap;

import org.junit.Test;

import static org.junit.Assert.*;

public class BulletTest {
    Bitmap b = null;
    int screenY = 500;
    int screenX = 75;

    @Test
    public void shoot() {
        Bullet bullet = new Bullet(screenY, screenX, b);

        boolean expected = false;
        boolean output = bullet.getStatus();

        assertEquals(expected, output);
    }


}