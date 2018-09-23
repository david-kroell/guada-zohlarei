package backslash.at.smartbank;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class BillDetailsActivity extends AppCompatActivity {

    static ArrayList<Double> suggestedPrices;
    EditText editTextTitle, editTextDescription;
    AutoCompleteTextView autoCompleteTextViewPrice;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        setTitle("Edit Receip");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        autoCompleteTextViewPrice = findViewById(R.id.autoCompleteTextViewPrice);

        Bundle extras = getIntent().getExtras();
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
                Double price = null;
                try
                {
                    price = Double.parseDouble(autoCompleteTextViewPrice.getText().toString());
                }
                catch(NumberFormatException e) {}

                if(price != null)
                    BillsFragment.volleyRequestHandlerBills.uploadBill(new Bill(editTextTitle.getText().toString(), image, price, editTextDescription.getText().toString()), MainActivity.user.getToken());
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
