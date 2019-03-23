package cs.spaceinvaders.entity;

import android.graphics.RectF;

public abstract class Entity {

    private RectF rectF;




    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }


}
