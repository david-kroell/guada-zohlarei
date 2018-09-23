package backslash.at.smartbank;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BillsFragment extends Fragment implements IVolleyCallbackBills{

    View view;
    public static VolleyRequestHandlerBills volleyRequestHandlerBills;
    List<Bill> allBills;
    static final int REQUEST_IMAGE_CAPTURE = 3113;
    static final int REQUEST_SAVE_BILL = 1337;
    String mCurrentPhotoPath;
    ListView listViewBills;
    ArrayAdapter<Bill> arrayAdapter;

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

        listViewBills = view.findViewById(R.id.listViewBills);

        allBills = new ArrayList<Bill>();

        volleyRequestHandlerBills = new VolleyRequestHandlerBills(getContext(),this);
        volleyRequestHandlerBills.getAllBills(MainActivity.user.getToken());
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            if(prices.size() > 0)
                launchDetailsView.putStringArrayListExtra("prices", prices);
            startActivityForResult(launchDetailsView, REQUEST_SAVE_BILL);
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

    @Override
    public void getAllBills(List<Bill> bills) {
        this.allBills = bills;
        arrayAdapter = new ArrayAdapter<Bill>(getActivity(), R.layout.listitem_bill, R.id.textViewBillItemTitle, allBills) {
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                final View v = super.getView(position, convertView, parent);
                TextView title = v.findViewById(R.id.textViewBillItemTitle);
                TextView price = v.findViewById(R.id.textViewBillItemValue);
                final ImageView imageView = v.findViewById(R.id.imageViewThumb);
                title.setText(allBills.get(position).getTitle());
                price.setText(allBills.get(position).getPrice() + "€");
                final String url = "http://172.31.204.151:80/bills/image/" + allBills.get(position).getImage();
                /*Target target = new Target() {

                    @Override
                    public void onPrepareLoad(Drawable arg0) {
                        Log.d("BITMAPLOAD", "Preparing to load " + url);
                    }

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                        Log.d("BITMAPLOAD", arg1.name());
                        allBills.get(position).setBitmap(bitmap);
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception ex, Drawable arg0) {
                        Log.d("BITMAPLOAD", ex.getMessage());
                    }
                };*/

                Picasso.get().load(url).into(imageView);
                //Picasso.get().load("http://172.31.204.151:80/bills/image/" + allBills.get(position).getImage()).into(imageView);
                /*else
                    imageView.;*/
                /*else {
                    LoadImageTask loader = new LoadImageTask();
                    loader.setBill(allBills.get(position));
                    loader.execute("http://172.31.204.151:80/bills/image/" + allBills.get(position).getImage());
                }*/

                return v;
            }
        };
        listViewBills.setAdapter(arrayAdapter);
        listViewBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetails = new Intent(getActivity(), BillDetailsActivity.class);
                showDetails.putExtra("editDisable", true);
                showDetails.putExtra("id", allBills.get(position).id);
                showDetails.putExtra("title", allBills.get(position).title);
                showDetails.putExtra("price", allBills.get(position).price);
                showDetails.putExtra("description", allBills.get(position).description);
                showDetails.putExtra("image", allBills.get(position).image);
                showDetails.putExtra("bitmap", allBills.get(position).getBitmap());
                startActivity(showDetails);
            }
        });
    }

    @Override
    public void uploadBillSuccess(Bill b) {
        this.allBills.add(b);
    }

    @Override
    public void billError(String error) {
        Toast.makeText(getContext(),"Error with bills", Toast.LENGTH_LONG).show();
    }
}
