package cs.spaceinvaders.entity;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class MovingEntity extends Entity {
    private PointF position;
    private float y;
    private int width ;
    private float height;

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public abstract void update(long fps);

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public float getY() {
        return position.y;
    }

    public void setPositionY(float y) {
        this.y = y;
    }

    public float getPositionY() {
       return this.y;
    }

    public void setY(float y) {
        this.position.y = y;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Used to check if an entity has collided with this one, being it any moving entity
     * @param hitbox
     * @return
     */
    public boolean hasCollided(RectF hitbox){
        return hitbox.intersect(getRectF());
    }

    public void updateRect(){
        getRectF().left = getX();
        this.getRectF().right = getX() + width;
        getRectF().top = getY();
        getRectF().bottom = getY() + height;
    }




}
