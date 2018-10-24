package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Enemy {

    RectF rect;

    Random generator = new Random();

    //Bitmaps para la animación
    private Bitmap anim1;
    private Bitmap anim2;

    //Tamaño
    private float length;
    private float height;

    //Coordenadas del invader
    private float x;
    private float y;

    // Movimiento
    private float enemySpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Movimiento y dirección
    private int enemyMoving = RIGHT;

    boolean isVisible;

    public Enemy(Context context, int row, int column, int screenX, int screenY) {

        rect = new RectF();

        length = screenX / 20;
        height = screenY / 20;

        isVisible = true;

        int padding = screenX / 25;

        x = column * (length + padding);
        y = row * (length + padding/4);

        // Animaciones
        anim1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderstart);
        anim2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderend);

        // Ajusta el tamaño de los invaders la resolución de la pantalla
        anim1 = Bitmap.createScaledBitmap(anim1,
                (int) (length),
                (int) (height),
                false);

        anim2 = Bitmap.createScaledBitmap(anim2,
                (int) (length),
                (int) (height),
                false);

        // Velocidad en píxeles por segundo de los invaders
        enemySpeed = 40;
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

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public void update(long fps){
        if(enemyMoving == LEFT){
            x = x - enemySpeed / fps;
        }

        if(enemyMoving == RIGHT){
            x = x + enemySpeed / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;



    }

    public void enemyCicle(){
        if(enemyMoving == LEFT){
            enemyMoving = RIGHT;
        }else{
            enemyMoving = LEFT;
        }

        y = y + height;

        enemySpeed = enemySpeed * 1.18f;
    }

    public boolean randomShot(float playerShipX, float playerShipLength){

        int randomNumber = -1;

        // Si está cerca del jugador
        if((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)) {

            // Una probabilidad de 1 en 500 de disparar
            randomNumber = generator.nextInt(150);
            if(randomNumber == 0) {
                return true;
            }

        }

        // Disparo aleatorio que no depende del player en 5000
        randomNumber = generator.nextInt(2000);
        if(randomNumber == 0){
            return true;
        }

        return false;
    }

}
