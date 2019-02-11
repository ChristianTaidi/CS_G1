package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

public class EnemySpecial {

    //Detector de impactos
    private RectF rect;

    private int shootsCount;

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

    public EnemySpecial(Context context, int screenX, int screenY, Bitmap b){

        rect = new RectF();

        maxX=screenX;
        maxY=screenY;

        length = screenX/10;
        height = screenY/10;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX;
        y = screenY-height*2;

        // Inicializa el bitmap
        bitmap = b;

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

    public void update(long fps){

            x = x - shipVel / fps;

        // Actualiza rect el cual es usado para detectar impactos
        rect.left = x;
        rect.right = x + length;

    }

}
