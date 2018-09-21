package backslash.at.smartbank;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_overview:
                                //selectedFragment = OverviewFragment.newInstance();
                                //break;
                                Toast.makeText(MainActivity.this, "overview", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_bills:
                                Toast.makeText(MainActivity.this, "bills", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_stocks:
                                Toast.makeText(MainActivity.this, "stocks", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        //transaction.replace(R.id.frame_layout, selectedFragment);
                        //transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_layout, ItemOneFragment.newInstance());
//        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BottomNav", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BottomNav", "Unable to change value of shift mode", e);
            }
        }
    }
}
