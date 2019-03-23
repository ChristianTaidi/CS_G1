package cs.spaceinvaders.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

public class Enemy extends MovingEntity{



    Random generator = new Random();

    //Bitmaps para la animación


    //Tamaño
    private float length;
    private float height;

    //Coordenadas del invader

    private int row;
    private int column;
    private int padding;

    private boolean isSpawned;

    // Movimiento
    private float enemySpeed;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // Movimiento y dirección
    private int enemyMoving = RIGHT;
    private static final float MAX_SPEED = 350f;
    private static final int MAX_PROBABILITY = 15;
    boolean isVisible;

    public Enemy(Context context, int row, int column, int screenX, int screenY, Bitmap a1, Bitmap a2, Bitmap a3, Bitmap a4) {
        isSpawned = false;
        this.row = row;
        this.column = column;

        setRectF( new RectF());

        length = screenX / 20;
        height = screenY / 20;

        this.padding = screenX / 25;

        setPosition(new PointF(column * (length + padding),row * (length + padding/4)));

        enemySpeed = 80;
    }

    public void setOff(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
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
            getPosition().offset( - enemySpeed / fps,0);
            column = (int)getPosition().x/(int)(length+padding);
        }

        if(enemyMoving == RIGHT){
            getPosition().offset( + enemySpeed / fps,0);
            column = (int)getPosition().x/(int)(length+padding);
        }

        // Actualiza rect el cual es usado para detectar impactos
        updateRect();
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
            getPosition().offset( 10,0);
        }else{
            enemyMoving = LEFT;
            getPosition().offset(- 10,0);
        }
        getPosition().offset(0, height);
        row = (int)getPosition().y/((int)length+padding/4);

        if(aux < MAX_SPEED) {
            enemySpeed = aux;
        }
        else {
            enemySpeed = MAX_SPEED;
        }
    }

    public boolean randomShot(float playerShipX, float playerShipLength){

        int randomNumber = -1;

        // Si está cerca del jugador
        if((playerShipX + playerShipLength > getPosition().x &&
                playerShipX + playerShipLength < getPosition().x + length) || (playerShipX > getPosition().x && playerShipX < getPosition().x + length)) {

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
