package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class Buttons {
    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;


    public Buttons(Context context, int screenX, int screenY, int id){


        rect = new RectF();

        length = screenX/10;
        height = screenY/10;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX;
        y = screenY;

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

    public float getLength(){
        return length;
    }


}
