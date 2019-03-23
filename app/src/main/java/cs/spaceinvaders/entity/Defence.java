package cs.spaceinvaders.entity;

import android.graphics.RectF;

public class Defence extends Entity{


    private boolean isDefending;

    public Defence(int row, int column, int shelterNumber, int screenX, int screenY){

        int width = screenX / 40;
        int height = screenY / 90;

        isDefending = true;

        int brickPadding = 1;

        //Número de defensas
        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY /8 * 3);

        this.setRectF( new RectF(column * width + brickPadding +
                (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + startHeight,
                column * width + width - brickPadding +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight));
    }



    public void destoyDefence(){
        isDefending = false;
    }

    public void resetDefence() {isDefending = true; }

    public boolean getActive(){
        return isDefending;
    }
}
