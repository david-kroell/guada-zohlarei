package backslash.at.smartbank;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

public class Bill {
    public int Id;
    public String Description;
    public Double Price;
    public String Image;
    public String Title;

    public Bill(String Title, Bitmap Image, Double Price, String Description) {
        this.Title = Title;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        Base64.encodeBase64(byteArray);
        this.Image = new String(Base64.encodeBase64(byteArray));
        this.Price = Price;
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public Double getPrice() {
        return Price;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }
}
