package backslash.at.smartbank;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements IVolleyCallbackLogin, IVolleyCallbackAccounts {

    EditText mUserName;
    EditText mPassword;
    String username;
    private ProgressBar spinner;
    VolleyRequestHandlerLogin volleyRequestHandlerLogin;
    VolleyRequestHandlerAccounts volleyRequestHandlerAccounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mUserName = findViewById(R.id.editTextUsername);
        mPassword = findViewById(R.id.editTextPassword);
        volleyRequestHandlerLogin = new VolleyRequestHandlerLogin(this.getApplicationContext(),this);
        volleyRequestHandlerAccounts = new VolleyRequestHandlerAccounts(this.getApplicationContext(),this);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
    }

    public void onClick_ButtonLogin(View v) {
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if(!username.equals("") && !password.equals("")) {
            this.username = username;
            volleyRequestHandlerLogin.Authenticate(username,password);
            spinner.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Please enter a valid username and password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccess(String token) {
        Log.d("Login SmartBank", "login successful");
        if (!token.isEmpty()) {
            MainActivity.user = new User(this.username, token);
            volleyRequestHandlerAccounts.getAllAccounts(token);
            /*Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            finish();*/
        } else {
            Toast.makeText(this, "Invalid login credentials!",Toast.LENGTH_SHORT).show();

        }
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void loginError(Integer errorCode) {
        switch (errorCode) {
            case 9999:
                Toast.makeText(this, "No network connection!",Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText(this, "Invalid login credentials!",Toast.LENGTH_SHORT).show();
                break;
            case 413:
                Toast.makeText(this, "Service unavailable, check SLA!", Toast.LENGTH_SHORT).show();
                break;
        }
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void getAllAccounts(List<BankAccount> list) {
        MainActivity.bankAccounts = list;
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void problemOccured(String errorMessage) {
        Toast.makeText(this,"accounts failed",Toast.LENGTH_LONG).show();
        spinner.setVisibility(View.GONE);
    }
}
