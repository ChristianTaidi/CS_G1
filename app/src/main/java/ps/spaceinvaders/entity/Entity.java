package ps.spaceinvaders.Entity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.List;

public abstract class Entity {
    private RectF rectF;




    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }


}
