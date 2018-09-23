package backslash.at.smartbank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class LoadImageTask extends AsyncTask<String, String, Bitmap> {

    ImageView imageView;
    Bill bill;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected Bitmap doInBackground(String... args) {
        Bitmap image = null;
        if(args.length == 1){
            try {
                URL myURL = new URL(args[0]);
                HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
                myURLConnection.setRequestProperty ("Authorization", "Bearer " + MainActivity.user.getToken());
                image = BitmapFactory.decodeStream(myURLConnection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return image;
    }
    protected void onPostExecute(Bitmap image) {
        if(image != null){
            bill.setBitmap(image);
        }
    }

    protected  void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    protected void setBill(Bill bill) {
        this.bill = bill;
    }
}
