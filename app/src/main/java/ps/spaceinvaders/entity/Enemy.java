package ps.spaceinvaders.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.Random;

public class Enemy {

    private RectF rect;

    Random generator = new Random();

    //Bitmaps para la animación
    private Bitmap anim1;
    private Bitmap anim2;
    private Bitmap anim3;
    private Bitmap anim4;

    //Tamaño
    private float length;
    private float height;

    //Coordenadas del invader
    private float x;
    private float y;

    private int row;
    private int column;
    private int padding;

    private boolean isSpawned;

    // Movimiento
    private float enemySpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Movimiento y dirección
    private int enemyMoving = RIGHT;
    private final float MAX_SPEED = 350f;
    private final int MAX_PROBABILITY = 15;
    boolean isVisible;

    public Enemy(Context context, int row, int column, int screenX, int screenY, Bitmap a1, Bitmap a2, Bitmap a3, Bitmap a4) {
        isSpawned = false;
        this.row = row;
        this.column = column;

        rect = new RectF();

        length = screenX / 20;
        height = screenY / 20;

        //isVisible = true;

        this.padding = screenX / 25;

        x = column * (length + padding);
        y = row * (length + padding/4);

        anim1 = a1;

        anim2 = a2;

        anim3 = a3;

        anim4 = a4;

        enemySpeed = 80;
    }

    public void setOff(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return anim1;
    }

    public Bitmap getBitmap2(){
        return anim2;
    }

    public Bitmap getBitmap3(){
        return anim3;
    }

    public Bitmap getBitmap4(){
        return anim4;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public int getEnemyMoving() {
        return enemyMoving;
    }

    public void setEnemyMoving(int dir) {
        enemyMoving = dir;
    }

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public float getHeight() {return height;}

    public int getPadding() {
        return padding;
    }

    public float getLength(){
        return length;
    }

    public void update(long fps){
        if(enemyMoving == LEFT){
            x = x - enemySpeed / fps;
            column = (int)x/(int)(length+padding);
        }

        if(enemyMoving == RIGHT){
            x = x + enemySpeed / fps;
            column = (int)x/(int)(length+padding);
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }

    public void angryEnemie(int killedEnemies){
        float aux = enemySpeed;
        aux += killedEnemies*0.5f;
        if(aux < MAX_SPEED) {
            enemySpeed = aux;
        }
        else {
            enemySpeed = MAX_SPEED;
        }
    }

    public void enemyCicle(){
        float aux = enemySpeed;
        if(enemyMoving == LEFT){
            enemyMoving = RIGHT;
            x += 10;
        }else{
            enemyMoving = LEFT;
            x -= 10;
        }
        y = y + height;
        row = (int)y/((int)length+padding/4);

        //aux *= 1.15f;
        if(aux < MAX_SPEED) {
            enemySpeed = aux;
        }
        else {
            enemySpeed = MAX_SPEED;
        }
    }

    public boolean randomShot(float playerShipX, float playerShipLength, int killedEnemies){

        int randomNumber = -1;

        // Si está cerca del jugador
        if((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)) {

            // Una probabilidad de 1 en 150 de disparar
            randomNumber = generator.nextInt(100);
            if(randomNumber < 0){
                randomNumber = generator.nextInt(MAX_PROBABILITY);
            }

            if(randomNumber == 0) {
                return true;
            }

        }

        return false;
    }

}
