package cs.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

import org.junit.Before;
import org.junit.Test;

import cs.spaceinvaders.entity.Bullet;
import cs.spaceinvaders.entity.MovingEntity;

import static org.junit.Assert.*;

public class BulletTest {
    Bitmap b;
    int screenY;
    int screenX;
    Context context;
    Bullet bullet;

    @Before
    public void setUp() {
        b = null;
        screenY = 500;
        screenX = 75;
        context = null;
        bullet = new Bullet( screenY, screenX, b);
    }

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

        //bucles for para probar distintos valores
        for (int y=0; y<500; y++){
            for (int height=0; height<1000; height++){
                bullet.setY(y);
                bullet.setHeight(height);
                for (int l=0; l<=1; l++){
                    bullet.setShotDir(l);
                    if (l==1)
                        expected = y+height;
                    else
                        expected = y;
                    output = bullet.getImpactPointY();
                    assertEquals(expected, output, delta);
                }
            }
        }
    }

    @Test
    public void getImpactPointX(){
        double delta = 0.1;
        float expected;
        float output;

        //bucles for para probar distintos valores
        for (int x=0; x<100; x++){
            for (int width=0; width<1000; width++){
                bullet.setX(x);
                bullet.setWidth(width);

                for (int dir=0; dir<=1; dir++) {
                    bullet.setShotDir(dir);
                    if (dir == 1)
                        expected = x + width;
                    else
                        expected = x;
                    output = bullet.getImpactPointX();
                    assertEquals(expected, output, delta);
                }
            }
        }
    }

    @Test
    public void rectFCorrectX(){
        float outputX1, outputX2;
        float expectedX1, expectedX2;
        double delta = 0.1;
        RectF output;

        //bucles for para probar distintos valores
        for (int x=0; x<100; x++) {
            for (int width = 0; width < 1000; width++) {
                for (int fps = 0; width < 120; width++) {
                    for (int dir = 0; dir<+ 1; dir++) {
                        bullet.setWidth(width);
                        bullet.setX(x);
                        bullet.setShotDir(dir);

                        bullet.update(fps);
                        output = bullet.getRectF();

                        outputX1 = output.left;
                        outputX2 = output.right;
                        expectedX1 = x;
                        expectedX2 = x + width;

                        assertEquals(expectedX1, outputX1, delta);
                        assertEquals(expectedX2, outputX2, delta);
                    }
                }

            }
        }
    }

    @Test
    public void rectFCorrectY(){
        float outputY1, outputY2;
        float expectedY1, expectedY2;
        double delta = 0.1;
        RectF output;

        //bucles for para probar distintos valores
        for (int speed=0; speed<10; speed++) {
            for (int y = 0; y < 100; y++) {
                for (int height = 0; height < 1000; height++) {
                    for (int fps = 1; height < 120; height++) {
                        for (int dir = 0; dir < +1; dir++) {
                            bullet.setHeight(height);
                            bullet.setY(y);
                            bullet.setShotDir(dir);
                            bullet.setSpeed(speed);
                            bullet.update(fps);

                            output = bullet.getRectF();
                            outputY1 = output.bottom;
                            outputY2 = output.top;
                            if (dir == 0) {
                                expectedY1 = (y-speed/fps) + height;
                                expectedY2 = (y-speed/fps);
                            } else {
                                expectedY1 = (y+speed/fps) + height;
                                expectedY2 = (y+speed/fps);
                            }

                            assertEquals(expectedY1, outputY1, delta);
                            assertEquals(expectedY2, outputY2, delta);
                        }
                    }

                }
            }
        }
    }


}