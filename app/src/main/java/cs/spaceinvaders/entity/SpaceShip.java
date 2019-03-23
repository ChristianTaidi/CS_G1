package cs.spaceinvaders.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

public class SpaceShip extends MovingEntity{

    private boolean isVulnerable;
    private int tpTime;
    //Detector de impactos
    private RectF rect;

    private int shootsCount;


    private int maxX;
    private int maxY;

    private float length;
    private float height;

    private float x;
    private float y;

    private float shipVel;

    //Direcciones
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int DOWN = 3;
    public final int UP = 4;

    //Estado actual de la nave
    private int shipMoving = STOPPED;

    public SpaceShip(Context context, int screenX, int screenY, Bitmap b){
        isVulnerable = true;
        tpTime = -1;

        rect = new RectF();

        shootsCount = 0;

        maxX=screenX;
        maxY=screenY;

        length = screenX/8;
        height = screenY/15;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX/20*9;
        y = screenY-height*2;

        // Inicializa el bitmap
        // velocidad nave en pixeles por segundo
        shipVel = 500;
    }

    public RectF getRect(){

        return rect;
    }

    public float getHeight() {
        return height;
    }

    // define nuestra nave espacial para que este disponible en View

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public void resetSpacechip() {
        x = maxX / 2;
        y = maxY-height;
    }

    public void addShootsCount() {
            this.shootsCount++;
    }

    public void resetShootsCount() {
        this.shootsCount = 0;
    }

    public int getShootsCount() {
        return this.shootsCount;
    }

    public float getLength(){
        return length;
    }

    // Establecer dirección de la nave
    public void setMovementState(int state){
        shipMoving = state;
    }

    public void update(long fps){
        if(shipMoving == LEFT && x>0){
            x = x - shipVel / fps;
        }

        if(shipMoving == RIGHT && x<maxX-length){
            x = x + shipVel / fps;
        }
        if(shipMoving == UP && y>0){
            y = y - shipVel / fps;
        }

        if(shipMoving == DOWN && y<maxY-height-80){
            y = y + shipVel / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void teleport(){
         x = (int) (Math.random() * (maxX-length)) + 1;
         y = (int) (Math.random() * (maxY-height-80)) + 1;

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public void setVulnerable(boolean b){
        isVulnerable = b;
    }

    public void setRandomTp(){
        tpTime =  (int) (Math.random() * 10) + 5;
    }

    public void setTpTime(int n){
        this.tpTime = n;
    }

    public int getTpTime() {
        return tpTime;
    }

}
