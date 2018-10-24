package ps.spaceinvaders;

import android.graphics.RectF;

public class Bullet {
    private float x;
    private float y;

    private RectF rect;

    //Dirección de disparo
    public final int UP = 0;
    public final int DOWN = 1;

    int shotDir = -1;
    float speed = 350;

    private int width = 1;
    private int height;

    private boolean isActive;

    public Bullet(int screenY){

        height = screenY /20;
        isActive = false;

        rect = new RectF();
    }

    public RectF getRect(){
        return rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY() {
        if (shotDir == DOWN) {
            return y + height;
        } else {
            return y;
        }
    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            shotDir = direction;
            isActive = true;
            return true;
        }

        return false;
    }

    public void update(long fps){

        // Movimiento arriba o abajo
        if(shotDir == UP){
            y = y - speed / fps;
        }else{
            y = y + speed / fps;
        }

        // Actualiza rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }



}
