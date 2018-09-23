package backslash.at.smartbank;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class BillDetailsActivity extends AppCompatActivity {

    static ArrayList<Double> suggestedPrices;
    static Boolean editDisable;
    EditText editTextTitle, editTextDescription;
    AutoCompleteTextView autoCompleteTextViewPrice;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        setTitle("Edit Receipt");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        autoCompleteTextViewPrice = findViewById(R.id.autoCompleteTextViewPrice);

        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("editDisable")) {
            editDisable = extras.getBoolean("editDisable");
            if(editDisable) {
                editTextTitle.setFocusable(false);
                editTextDescription.setFocusable(false);
                autoCompleteTextViewPrice.setFocusable(false);
            }
        }

        if(extras.containsKey("title")) {
            editTextTitle.setText(extras.getString("title"));
        }

        if(extras.containsKey("price")) {
            autoCompleteTextViewPrice.setText(Double.toString(extras.getDouble("price")));
        }

        if(extras.containsKey("description")) {
            editTextDescription.setText(extras.getString("description"));
        }

        if(extras.containsKey("bitmap")) {
            Bitmap bitmap = extras.getParcelable("bitmap");
            AppBarLayout abl = findViewById(R.id.app_bar);
            abl.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

        /*if(extras.containsKey("image")) {
            final String url = "http://172.31.204.151:80/bills/image/" + extras.getString("image");
            Target target = new Target() {

                @Override
                public void onPrepareLoad(Drawable arg0) {
                    Log.d("BITMAPLOAD", "Preparing to load " + url);
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                    Log.d("BITMAPLOAD", arg1.name());
                    AppBarLayout abl = findViewById(R.id.app_bar);
                    abl.setBackground(new BitmapDrawable(getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Exception ex, Drawable arg0) {
                    Log.d("BITMAPLOAD", ex.getMessage());
                }
            };

            Picasso.get().load(url).into(target);
        }*/

        if(extras.containsKey("pictureUri")) {
            image = BitmapFactory.decodeFile((String) extras.get("pictureUri"));
            // bind header image
            AppBarLayout abl = findViewById(R.id.app_bar);
            abl.setBackground(new BitmapDrawable(getResources(), image));
        }

        if(extras.containsKey("prices")) {
            suggestedPrices = new ArrayList<>();

            ArrayList<String> prices = (ArrayList<String>)extras.getStringArrayList("prices");
            for(String price : prices) {
                try {
                    double priceAsDouble = Double.parseDouble(price);
                    if(!suggestedPrices.contains(priceAsDouble))
                        suggestedPrices.add(priceAsDouble);
                }
                catch(Exception ex) {
                    Log.e("SmartBank Parsing", "Failed to parse the following text as double: " + price);
                }
            }
            try {
                autoCompleteTextViewPrice.setText(String.format("%.2f", Collections.max(suggestedPrices)));
            } catch (Exception ex) {}
        }

        FloatingActionButton fab = findViewById(R.id.fabEditBill);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editDisable)  {
                    Double price = null;
                    try
                    {
                        price = Double.parseDouble(autoCompleteTextViewPrice.getText().toString());
                    }
                    catch(NumberFormatException e) {}

                    if(price != null)
                        BillsFragment.volleyRequestHandlerBills.uploadBill(new Bill(editTextTitle.getText().toString(), image, price, editTextDescription.getText().toString()), MainActivity.user.getToken());
                }

                finish();
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent main = new Intent();
            setResult(RESULT_CANCELED, main);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
