package ps.spaceinvaders;

import android.graphics.Bitmap;

import org.junit.Test;

import static org.junit.Assert.*;

public class BulletTest {
    Bitmap b = null;
    int screenY = 500;
    int screenX = 75;
    Bullet bullet = new Bullet(screenY, screenX, b);

    @Test
    public void setInactive() {
        bullet.setInactive();
        boolean expected = false;
        boolean output = bullet.getStatus();

        assertEquals(expected, output);
    }

    @Test
    public void getImpactPointY(){
        bullet.setX(50);
        bullet.setY(50);
        bullet.setHeight(500);
        bullet.setShotDir(1);

        float expected = 50+500;
        float output = bullet.getImpactPointY();
        double delta = 0.1;

        assertEquals(expected, output, delta);
    }


}