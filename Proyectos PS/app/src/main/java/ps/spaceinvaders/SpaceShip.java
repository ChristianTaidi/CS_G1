package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class SpaceShip {

    //Detector de impactos
    private RectF rect;

    private Bitmap bitmap;
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

    public SpaceShip(Context context, int screenX, int screenY){


        rect = new RectF();

        maxX=screenX;
        maxY=screenY;

        length = screenX/10;
        height = screenY/10;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX / 2;
        y = screenY-height;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

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
    public Bitmap getBitmap(){
        return bitmap;
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

        if(shipMoving == DOWN && y<maxY-height ){
            y = y + shipVel / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }




}
