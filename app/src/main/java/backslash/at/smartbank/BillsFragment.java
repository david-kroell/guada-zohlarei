package backslash.at.smartbank;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class BillsFragment extends Fragment implements IV{

    View view;

    static final int REQUEST_IMAGE_CAPTURE = 3113;
    static final int REQUEST_SAVE_BILL = 1337;
    String mCurrentPhotoPath;

    public static BillsFragment newInstance() {
        BillsFragment fragment = new BillsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bills, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Hello from FAB", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Snackbar.make(view, "An error occured while creating the image file", Snackbar.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "at.backslash.smartbank.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    @TargetApi(26)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("BillDetection", "received result");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("BillDetection", "result was ok");

            // Get the dimensions of the bitmap
            /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inPurgeable = true;*/

            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

            /*ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageBitmap(imageBitmap);*/
            Log.d("BillDetection", "building frame");
            Frame frame =  new Frame.Builder().setBitmap(imageBitmap).build();
            Log.d("BillDetection", "initializing recognizer");
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
            SparseArray<TextBlock> texts = textRecognizer.detect(frame);
            ArrayList<String> alltexts = new ArrayList<>();
            Log.d("BillDetection", "found " + texts.size() + " texts");
            ArrayList<String> prices = new ArrayList<>();
            for (int i = 0; i < texts.size(); ++i) {
                TextBlock item = texts.valueAt(i);
                if (item != null && item.getValue() != null) {
                    String testcase = item.getValue().replace('\n', ' ').replace("-", "0");
                    alltexts.add(testcase);
                    String[] testCaseParts = testcase.split("\\s+");
                    Log.d("BillDetection", "Testcase \"" + testcase + "\" has " + testCaseParts.length + " parts");
                    for(String testCasePart : testCaseParts) {
                        if(testCasePart.matches("^\\d{1,}[,.]\\d{2}$"))
                            prices.add(testCasePart);
                    }
                }
            }
            Log.d("BillDetection", prices.size() + " testcases match");
            Intent launchDetailsView = new Intent(getActivity(), BillDetailsActivity.class);
            launchDetailsView.putExtra("pictureUri", mCurrentPhotoPath);
            launchDetailsView.putStringArrayListExtra("prices", prices);
            startActivityForResult(launchDetailsView, REQUEST_SAVE_BILL);
            //Log.d("BillDetection", "Found prices: " + );
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
