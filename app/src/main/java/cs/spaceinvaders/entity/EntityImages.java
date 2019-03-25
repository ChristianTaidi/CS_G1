package cs.spaceinvaders.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class EntityImages {

    private List<Bitmap> shipImages;

    private List<Bitmap> enemyColor1;

    private List<Bitmap> enemyColor2;

    private Bitmap shipImage;

    private Bitmap enemyImage;

    private Bitmap bulletImage;

    public EntityImages(Bitmap spaceshipBitmap, Bitmap enemyAnim1Bitmap, Bitmap enemyAnim2Bitmap, Bitmap enemyAnim3Bitmap, Bitmap enemyAnim4Bitmap, Bitmap bulletImage) {

        this.shipImages = new ArrayList<>();
        shipImages.add(spaceshipBitmap);
        shipImage=spaceshipBitmap;

        this.enemyColor1 = new ArrayList<>();
        enemyColor1.add(enemyAnim1Bitmap);
        enemyColor1.add(enemyAnim2Bitmap);

        this.enemyColor2 = new ArrayList<>();
        enemyColor2.add(enemyAnim3Bitmap);
        enemyColor2.add(enemyAnim4Bitmap);

        this.enemyImage=enemyColor1.get(0);

        this.bulletImage=bulletImage;
    }

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

    public Bitmap getBulletImage() {
        return bulletImage;
    }

    public void setBulletImage(Bitmap bulletImage) {
        this.bulletImage = bulletImage;
    }

    public void animate(){
        if(this.getEnemyImage().equals(this.enemyColor1.get(0))){
            this.setEnemyImage(enemyColor1.get(1));
        }else{
            this.setEnemyImage(enemyColor1.get(0));
        }
    }


    public void changeEnemyColor(){
        List<Bitmap> aux ;
        aux = enemyColor1;
        enemyColor1=enemyColor2;
        enemyColor2=aux;
    }
}
