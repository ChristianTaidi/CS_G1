package ps.spaceinvaders.Entity;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.List;

public abstract class MovingEntity extends Entity {
    private PointF position;

    private List<Bitmap> image;

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public List<Bitmap> getImage() {
        return image;
    }

    public void setImage(List<Bitmap> image) {
        this.image = image;
    }

    public abstract void update(long fps);
}
