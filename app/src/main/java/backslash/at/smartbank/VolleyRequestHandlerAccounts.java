package backslash.at.smartbank;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequestHandlerAccounts {
    private  static RequestQueue requestQueue;
    private IVolleyCallbackAccounts IcallbackAccounts;
    private String uri;
    private Boolean connected;
/*response.toString();
                                Gson g = new Gson();
                                BankAccount[] arr = g.fromJson(response.toString(), BankAccount[].class);
                                //g.fromJson(response.toString(),List<Accounts>)
                                //test = response.getJSONArray("");
                                String test = "lol";
                                IcallbackAccounts.getAllAccounts(Arrays.asList(arr));*/
    public VolleyRequestHandlerAccounts(Context context, IVolleyCallbackAccounts icallbackAccounts) {
        requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.IcallbackAccounts = icallbackAccounts;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null) {
            connected = cm.getActiveNetworkInfo().isConnected();
        } else {
            connected = false;
        }

        this.uri = "http://172.31.203.133:8081/v1/account";
    }

    public void getAllAccounts(String token){
        final String token2 = token;
        if(connected) {
            String url = this.uri;

            // Login
            Map<String, String> jsonParams = new HashMap<String, String>();

            //jsonParams.put("Authorization", "Bearer " + token);
            String test = jsonParams.toString();
            JsonArrayRequest postRequest = new JsonArrayRequest( Request.Method.GET, url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                response.toString();
                                Log.e("RESPONSE:", response.toString());
                                Gson g = new Gson();
                                BankAccount[] arr = g.fromJson(response.toString(), BankAccount[].class);
                                //g.fromJson(response.toString(),List<Accounts>)
                                //test = response.getJSONArray("");
                                String test = "lol";
                                IcallbackAccounts.getAllAccounts(Arrays.asList(arr));

                            } catch (Exception e) {
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
                    headers.put("Authorization","Bearer " + token2);
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
