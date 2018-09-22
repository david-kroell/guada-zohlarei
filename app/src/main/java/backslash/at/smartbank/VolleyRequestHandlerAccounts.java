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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequestHandlerAccounts {
    private  static RequestQueue requestQueue;
    private IVolleyCallbackAccounts IcallbackAccounts;
    private String uri;
    private Boolean connected;

    public VolleyRequestHandlerAccounts(Context context, IVolleyCallbackAccounts icallbackAccounts) {
        requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.IcallbackAccounts = icallbackAccounts;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = cm.getActiveNetworkInfo().isConnected();
        this.uri = "http://172.31.203.133:8081/v1/user/login";
    }

    public void getAllAccounts(String token){
        if(connected) {
            String url = this.uri;

            // Login
            Map<String, String> jsonParams = new HashMap<String, String>();

            jsonParams.put("Authorization", token);
            String test = jsonParams.toString();
            JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.GET, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("token");
                                //parse to object
                                Gson g = new Gson();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                IcallbackAccounts.problemOccured("Parsing error");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    IcallbackAccounts.problemOccured("network error");
                    error.printStackTrace();
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
            IcallbackAccounts.problemOccured("NETWORK_ERROR");
        }
    }
}
