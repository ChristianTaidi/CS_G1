package cs.spaceinvaders.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
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

    private Bitmap bulletImage;

    private int bounceCounts;

    public Bullet( int screenY, int screenX, Bitmap b){
        // Inicializa el bitmap
        setHeight( screenY/20);

        setPosition(new PointF());

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


    public int getShotDir() {
        return shotDir;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isEnemyBullet() {
        return enemyBullet;
    }

    public boolean isFriend() {
        return friend;
    }

    public Bitmap getBulletImage() {
        return bulletImage;
    }

    public void setBulletImage(Bitmap bulletImage) {
        this.bulletImage = bulletImage;
    }

    public float getImpactPointY() {
        if (shotDir == DOWN) {
            return getY() + getHeight();
        } else {
            return getY();
        }
    }

    public float getImpactPointX() {
        if (shotDir == DOWN) {
            return getX() + getWidth();
        } else {
            return getX();
        }
    }

    public void setShotDir(int dir){
        this.shotDir=dir;
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
