package ps.spaceinvaders;

import android.graphics.RectF;

public class Defence {

    private RectF rect;

    private boolean isDefending;

    public Defence(int row, int column, int shelterNumber, int screenX, int screenY){

        int width = screenX / 90;
        int height = screenY / 40;

        isDefending = true;

        int brickPadding = 0;

        //Número de defensas
        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY /8 * 3);

        rect = new RectF(column * width + brickPadding +
                (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + startHeight,
                column * width + width - brickPadding +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void destoyDefence(){
        isDefending = false;
    }

    public boolean getActive(){
        return isDefending;
    }
}
