package ps.spaceinvaders.Entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Bullet extends MovingEntity{


    //Dirección de disparo
    public final int UP = 0;
    public final int DOWN = 1;

    int shotDir = -1;
    float speed = 350;

    private boolean isActive;

    private boolean enemyBullet;
    private boolean friend;

    private int bounceCounts;

    public Bullet(Context context, int screenY, int screenX, Bitmap b){
        // Inicializa el bitmap
        setHeight( screenY/20);

        setBitmap(b);

        bounceCounts = 0;
        enemyBullet = false;
        friend = false;
        isActive = false;

        setRectF(new RectF());
    }


    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public void changeDirection() {
        if (shotDir == UP) {
            shotDir = DOWN;
        }
        else {
            shotDir = UP;
        }
    }

    public boolean getFriend() {
        return friend;
    }

    public void setFriend(boolean a) {
        friend = a;
    }


    public void setEnemyBullet(boolean active) {
        enemyBullet = active;
    }

    public boolean getEnemyBullet() {
        return enemyBullet;
    }

    public float getImpactPointY() {
        if (shotDir == DOWN) {
            return getY() + getHeight();
        } else {
            return getY();
        }
    }


    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            setX( startX);
            setY( startY + getHeight());
            shotDir = direction;
            isActive = true;
            return true;
        }

        return false;
    }

    public int getBounceCounts() {
        return bounceCounts;
    }

    public void updateBounceCounts() {
        bounceCounts++;
    }

    public void update(long fps){

        // Movimiento arriba o abajo
        if(shotDir == UP){
            getPosition().offset(0,  - speed / fps);
        }else{
            getPosition().offset(0, speed / fps);
        }

        // Actualiza rect
        updateRect();


    }



}
