package cs.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageEncoder {

    private Bitmap decodedImage;
    private String encodedImage;

    public ImageEncoder(Bitmap b) {
        this.decodedImage = b;
        this.encodedImage = encodeTobase64(b);
    }

    public ImageEncoder(String s) {
        this.decodedImage = decodeBase64(s);
        this.encodedImage = s;
    }

    private static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public Bitmap getDecodedImage() {
        return decodedImage;
    }

    public String getEncodedImage() {
        return encodedImage;
    }
}
