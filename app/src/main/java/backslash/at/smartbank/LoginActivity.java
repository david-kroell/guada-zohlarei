package backslash.at.smartbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity implements IVolleyCallbackLogin {

    EditText mUserName;
    EditText mPassword;
    VolleyRequestHandlerLogin volleyRequestHandlerLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mUserName = findViewById(R.id.editTextUsername);
        mPassword = findViewById(R.id.editTextPassword);
        volleyRequestHandlerLogin = new VolleyRequestHandlerLogin(this.getApplicationContext(),this);
    }

    public void onClick_ButtonLogin(View v) {
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        volleyRequestHandlerLogin.Authenticate(username,password);
    }

    @Override
    public void loginSuccess(String token) {
        Toast.makeText(this,token,Toast.LENGTH_SHORT).show();
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void loginError(Integer errorCode) {
        Toast.makeText(this,"oasch" + errorCode.toString(),Toast.LENGTH_SHORT).show();
    }
}
