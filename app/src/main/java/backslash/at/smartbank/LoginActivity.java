package backslash.at.smartbank;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements IVolleyCallbackLogin, IVolleyCallbackAccounts {

    EditText mUserName;
    EditText mPassword;
    String username;
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
    }

    public void onClick_ButtonLogin(View v) {
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if(!username.equals("") && !password.equals("")) {
            this.username = username;
            volleyRequestHandlerLogin.Authenticate(username,password);

        } else {
            Toast.makeText(this, "Please enter a valid username and password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccess(String token) {
        if (token != "INVALID") {
            MainActivity.user = new User(this.username, token);
            volleyRequestHandlerAccounts.getAllAccounts(token);
        } else {
            Toast.makeText(this, "Invalid login credentials!",Toast.LENGTH_SHORT).show();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    @Override
    public void loginError(Integer errorCode) {
        Toast.makeText(this, "Invalid login credentials!",Toast.LENGTH_SHORT).show();
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void getAllAccounts(List<BankAccount> list) {
        MainActivity.bankAccounts = list;
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void problemOccured(String errorMessage) {
        Toast.makeText(this,"accounts failed",Toast.LENGTH_LONG).show();
    }
}
