package ps.spaceinvaders.Entity;

import android.graphics.Bitmap;

import java.util.List;

public class EntityImages {

    private List<Bitmap> shipImages;

    private List<Bitmap> enemyColor1;

    private List<Bitmap> enemyColor2;

    private Bitmap shipImage;

    private Bitmap enemyImage;

    public List<Bitmap> getShipImages() {
        return shipImages;
    }

    public void setShipImages(List<Bitmap> shipImages) {
        this.shipImages = shipImages;
    }

    public List<Bitmap> getEnemyColor1() {
        return enemyColor1;
    }

    public void setEnemyColor1(List<Bitmap> enemyColor1) {
        this.enemyColor1 = enemyColor1;
    }

    public List<Bitmap> getEnemyColor2() {
        return enemyColor2;
    }

    public void setEnemyColor2(List<Bitmap> enemyColor2) {
        this.enemyColor2 = enemyColor2;
    }

    public Bitmap getShipImage() {
        return shipImage;
    }

    public void setShipImage(Bitmap shipImage) {
        this.shipImage = shipImage;
    }

    public Bitmap getEnemyImage() {
        return enemyImage;
    }

    public void setEnemyImage(Bitmap enemyImage) {
        this.enemyImage = enemyImage;
    }
    public void animate(){
        if(this.getEnemyImage().equals(this.enemyColor1.get(1))){
            this.setEnemyImage(enemyColor1.get(2));
        }else{
            this.setEnemyImage(enemyColor1.get(1));
        }
    }


    public void changeEnemyColor(){
        List<Bitmap> aux ;
        aux = enemyColor1;
        enemyColor1=enemyColor2;
        enemyColor2=aux;
    }
}
