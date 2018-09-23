package backslash.at.smartbank;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

public class Bill {
    public int id;
    public String description;
    public Double price;
    public String image;
    private Bitmap bitmap;
    public String title;

    public Bill(String title, Bitmap image, Double price, String description) {
        this.title = title;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        Base64.encodeBase64(byteArray);
        this.image = new String(Base64.encodeBase64(byteArray));
        this.price = price;
        this.description = description;
    }

    public Bill(String Title, String Image, Double Price, String Description) {
        this.title = Title;
        this.image = Image;
        this.price = Price;
        this.description = Description;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getBitmap() { return bitmap; }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
