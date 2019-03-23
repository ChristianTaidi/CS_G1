package cs.spaceinvaders.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Buttons {
    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    public Buttons(Context context, int screenX, int screenY, int id, float scaleX, float scaleY){

        rect = new RectF();

        length = screenX/10;
        height = screenY/10;

        x = scaleX;
        y = scaleY;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), id);

        // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
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

    public float getY() {
        return y;
    }

    public float getLength(){
        return length;
    }

    public float getHeight() {
        return height;
    }
}
