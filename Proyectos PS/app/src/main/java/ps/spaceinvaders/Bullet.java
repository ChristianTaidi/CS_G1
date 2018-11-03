package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Bullet {
    private float x;
    private float y;

    private RectF rect;

    private Bitmap bulletEnemy;
    private Bitmap bulletSpaceship;

    //Dirección de disparo
    public final int UP = 0;
    public final int DOWN = 1;

    int shotDir = -1;
    float speed = 350;

    private int width = 1;
    private float height;

    private float length;

    private boolean isActive;

    private boolean enemyBullet;
    private boolean friend;

    private int bounceCounts;

    public Bullet(Context context, int screenY, int screenX){
        // Inicializa el bitmap
        length = screenX/20;
        height = screenY/20;

        bulletEnemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bulletEnemy = Bitmap.createScaledBitmap(bulletEnemy,
                (int) (length),
                (int) (height),
                false);
        // Inicializa el bitmap

        bulletSpaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet2);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bulletSpaceship = Bitmap.createScaledBitmap(bulletSpaceship,
                (int) (length),
                (int) (height),
                false);

        bounceCounts = 0;
        enemyBullet = false;
        friend = false;
        //height = screenY /20;
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

    public Bitmap getBulletEnemy() {
        return bulletEnemy;
    }

    public Bitmap getBulletSpaceship() {
        return bulletSpaceship;
    }

    public void setEnemyBullet(boolean active) {
        enemyBullet = active;
    }

    public boolean getEnemyBullet() {
        return enemyBullet;
    }

    public float getImpactPointY() {
        if (shotDir == DOWN) {
            return y + height;
        } else {
            return y;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength() {
        return length;
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

    public int getBounceCounts() {
        return bounceCounts;
    }

    public void updateBounceCounts() {
        bounceCounts++;
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
