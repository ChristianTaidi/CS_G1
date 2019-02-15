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
        float expected;
        float output;

        for (int i=0; i<100; i++){
            for (int j=0; j<500; j++){
                for (int k=0; k<1000; k++){
                    bullet.setX(i);
                    bullet.setY(j);
                    bullet.setHeight(k);
                    for (int l=0; l<=1; l++){
                        bullet.setShotDir(l);
                        if (l==1)
                            expected = j+k;
                        else
                            expected = j;
                        output = bullet.getImpactPointY();
                        assertEquals(expected, output, delta);
                    }
                }
            }
        }
    }

    @Test
    public void getImpactPointX(){
        double delta = 0.1;
        float expected;
        float output;

        for (int i=0; i<100; i++){
            for (int j=0; j<500; j++){
                for (int k=0; k<1000; k++){
                    bullet.setX(i);
                    bullet.setY(j);
                    bullet.setWidth(k);
                    bullet.setShotDir(1);

                    for (int l=0; l<=1; l++) {
                        bullet.setShotDir(l);
                        if (l == 1)
                            expected = i + k;
                        else
                            expected = i;
                        output = bullet.getImpactPointX();
                        assertEquals(expected, output, delta);
                    }
                }
            }
        }
    }


}