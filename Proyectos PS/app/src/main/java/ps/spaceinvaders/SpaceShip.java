package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class SpaceShip {

    //Detector de impactos
    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private float shipVel;

    //Direcciones
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    //Estado actual de la nave
    private int shipMoving = STOPPED;

    public SpaceShip(Context context, int screenX, int screenY){


        rect = new RectF();

        length = screenX/10;
        height = screenY/10;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX / 2;
        y = screenY - 20;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // velocidad nave en pixeles por segundo
        shipVel = 350;
    }

    public RectF getRect(){

        return rect;
    }

    // define nuestra nave espacial para que este disponible en View
    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getLength(){
        return length;
    }

    // Establecer dirección de la nave
    public void setMovementState(int state){
        shipMoving = state;
    }

    public void update(long fps){
        if(shipMoving == LEFT){
            x = x - shipVel / fps;
        }

        if(shipMoving == RIGHT){
            x = x + shipVel / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }




}
