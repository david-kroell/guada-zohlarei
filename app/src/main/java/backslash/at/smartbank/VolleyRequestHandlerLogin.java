package backslash.at.smartbank;

import android.content.Context;
import android.net.ConnectivityManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestHandlerLogin {
    private  static RequestQueue requestQueue;
    private IVolleyCallbackLogin IcallbackLogin;
    private String uri;
    private Boolean connected;

    public VolleyRequestHandlerLogin(Context context, IVolleyCallbackLogin icallbackLogin) {
        requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.IcallbackLogin = icallbackLogin;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            connected = cm.getActiveNetworkInfo().isConnected();
        } else {
            connected = false;
        }

        this.uri = "http://172.31.203.133:8081/v1/user/login";
    }

    public void Authenticate(String username, String password) {

        if(connected) {
            String url = this.uri;

            // Login
            Map<String, String> jsonParams = new HashMap<String, String>();

            jsonParams.put("name", username);
            jsonParams.put("password", password);
            String test = jsonParams.toString();
            JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("token");

                                if(!token.equals("")) {
                                    IcallbackLogin.loginSuccess(token);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                IcallbackLogin.loginSuccess("INVALID");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse != null) {
                        NetworkResponse networkResponse = error.networkResponse;
                        IcallbackLogin.loginError(networkResponse.statusCode);
                        error.printStackTrace();
                    } else {
                        IcallbackLogin.loginError(413);
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("User-agent", System.getProperty("http.agent"));
                    return headers;
                }

            };
            postRequest.setTag("TAG");
            requestQueue.add(postRequest);
        } else {
            IcallbackLogin.loginError(9999);
        }
    }

}
