package ps.spaceinvaders.Entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends MovingEntity {


    Random generator = new Random();


    //Tamaño
    private float length;
    private float height;


    private int row;
    private int column;
    private int padding;

    private boolean isSpawned;

    // Movimiento
    private float enemySpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Movimiento y dirección
    private int enemyMoving = RIGHT;
    private final float MAX_SPEED = 350f;
    private final int MAX_PROBABILITY = 15;
    boolean isVisible;

    public Enemy(Context context, int row, int column, int screenX, int screenY, Bitmap a1, Bitmap a2, Bitmap a3, Bitmap a4) {
        isSpawned = false;
        this.row = row;
        this.column = column;

        this.setRectF(new RectF());

        length = screenX / 20;
        height = screenY / 20;

        //isVisible = true;

        this.padding = screenX / 25;

        this.setPosition(new PointF(column * (length + padding), row * (length + padding / 4)));

        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(a1);

        images.add(a2);

        images.add(a3);

        images.add(a4);

        this.setImage(images);

        enemySpeed = 80;
    }

    public void setOff() {
        isVisible = false;
    }

    public boolean getVisibility() {
        return isVisible;
    }


    public boolean isSpawned() {
        return isSpawned;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public int getEnemyMoving() {
        return enemyMoving;
    }

    public void setEnemyMoving(int dir) {
        enemyMoving = dir;
    }

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public float getHeight() {
        return height;
    }

    public int getPadding() {
        return padding;
    }

    public float getLength() {
        return length;
    }

    @Override
    public void update(long fps) {
        if (enemyMoving == LEFT) {
            this.getPosition().offset(-enemySpeed / fps, 0);
            column = (int) this.getPosition().x / (int) (length + padding);
        }

        if (enemyMoving == RIGHT) {
            this.getPosition().offset(enemySpeed / fps, 0);
            column = (int) this.getPosition().x / (int) (length + padding);
        }

        // Actualiza rect el cual es usado para detectar impactos
        this.getRectF().top = this.getPosition().y;
        this.getRectF().bottom = this.getPosition().y + height;
        this.getRectF().left = this.getPosition().x;
        this.getRectF().right = this.getPosition().x + length;
    }

    public void angryEnemie(int killedEnemies) {
        float aux = enemySpeed;
        aux += killedEnemies * 0.5f;
        if (aux < MAX_SPEED) {
            enemySpeed = aux;
        } else {
            enemySpeed = MAX_SPEED;
        }
    }

    public void enemyCicle() {
        float aux = enemySpeed;
        if (enemyMoving == LEFT) {
            enemyMoving = RIGHT;
            this.getPosition().offset(10, 0);
        } else {
            enemyMoving = LEFT;
            this.getPosition().offset(0, height);
            row = (int) this.getPosition().y / ((int) length + padding / 4);

            //aux *= 1.15f;
            if (aux < MAX_SPEED) {
                enemySpeed = aux;
            } else {
                enemySpeed = MAX_SPEED;
            }
        }
    }

        public boolean randomShot ( float playerShipX, float playerShipLength, int killedEnemies){

            int randomNumber = -1;

            // Si está cerca del jugador
            if ((playerShipX + playerShipLength > this.getPosition().x &&
                    playerShipX + playerShipLength < this.getPosition().x + length) || (playerShipX > this.getPosition().x && playerShipX < this.getPosition().x + length)) {

                // Una probabilidad de 1 en 150 de disparar
                randomNumber = generator.nextInt(100);
                if (randomNumber < 0) {
                    randomNumber = generator.nextInt(MAX_PROBABILITY);
                }

                if (randomNumber == 0) {
                    return true;
                }

            }

            return false;
        }
}
