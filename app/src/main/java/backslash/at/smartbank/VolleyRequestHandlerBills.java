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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequestHandlerBills {
    private  static RequestQueue requestQueue;
    private IVolleyCallbackBills iCallbackBills;
    private String uri;
    private Boolean connected;

    public VolleyRequestHandlerBills(Context context, IVolleyCallbackBills iVolleyCallbackBills) {
        requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.iCallbackBills = iVolleyCallbackBills;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = cm.getActiveNetworkInfo().isConnected();
        this.uri = "http://172.31.203.133:8081/v1/bill";
    }
    public void uploadBill(Bill b, String token){
        final String token2 = token;
        if(connected) {
            String url = this.uri;

            // Login
            Map<String, Object> jsonParams = new HashMap<String, Object>();

            jsonParams.put("Description", b.getDescription());

            jsonParams.put("Price", b.getPrice());
            jsonParams.put("Title", b.getTitle());
            jsonParams.put("Image", b.getImage());
            String test = jsonParams.toString();
            JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                               Gson g = new Gson();
                               Bill b = g.fromJson(response.toString(),Bill.class);
                               iCallbackBills.uploadBillSuccess(b);
                            } catch (Exception e) {
                                e.printStackTrace();
                                iCallbackBills.billError("INVALID");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    iCallbackBills.billError("error request");
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
            iCallbackBills.billError("NETWORK_ERROR");
        }
    }
    public void getAllBills(String token){
        final String token2 = token;
        if(connected) {
            String url = this.uri;
            JsonArrayRequest postRequest = new JsonArrayRequest( Request.Method.GET, url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                //Log.e("RESPONSE:", response.toString());
                                Gson g = new Gson();
                                Bill[] arr = g.fromJson(response.toString(), Bill[].class);

                                iCallbackBills.getAllBills(Arrays.asList(arr));

                            } catch (Exception e) {
                                e.printStackTrace();
                                iCallbackBills.billError("Parsing error");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    iCallbackBills.billError("network error");
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
            iCallbackBills.billError("NETWORK_ERROR");
        }
    }
}
