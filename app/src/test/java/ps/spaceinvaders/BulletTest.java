package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Test;

import static org.junit.Assert.*;

public class BulletTest {
    Bitmap b = null;
    int screenY = 500;
    int screenX = 75;
    Context context = null;
    Bullet bullet = new Bullet(context, screenY, screenX, b);

    @Test
    public void setInactive() {
        bullet.setInactive();
        boolean expected = false;
        boolean output = bullet.getStatus();

        assertEquals(expected, output);
    }

    @Test
    public void getImpactPointY(){
        double delta = 0.1;
        for (int i=0; i<100; i=i+1){
            for (int j=0; j<500; j=j+1){
                for (int k=0; k<1000; k=k+1){
                    bullet.setX(i);
                    bullet.setY(j);
                    bullet.setHeight(k);
                    bullet.setShotDir(1);

                    float expected = j+k;
                    float output = bullet.getImpactPointY();
                    assertEquals(expected, output, delta);
                }
            }
        }
    }


}