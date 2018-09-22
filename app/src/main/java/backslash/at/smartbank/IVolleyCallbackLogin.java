package backslash.at.smartbank;

import com.android.volley.NetworkResponse;

public interface IVolleyCallbackLogin {
    void loginSuccess(String token);
    void loginError(Integer errorCode);

}
