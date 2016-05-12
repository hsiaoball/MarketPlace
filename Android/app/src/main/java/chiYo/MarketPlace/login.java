package chiYo.MarketPlace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private static final String TAG = login.class.getSimpleName();
    private String backendURL = "http://kidvities-snowleo.rhcloud.com/";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chiYo.MarketPlace.R.layout.activity_login);

        queue = Volley.newRequestQueue(this);

        final EditText etUsername = (EditText) findViewById(chiYo.MarketPlace.R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(chiYo.MarketPlace.R.id.etPassWord);
        Button mBtLogin = (Button) findViewById(chiYo.MarketPlace.R.id.btLogin);
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(login.this, "",
                        "Login. Please wait...", true);
                StringRequest strRequest = new StringRequest(Request.Method.POST, backendURL + "login/",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject obj = null;
                                try {
                                    obj = new JSONObject(response);
                                    if (obj.has("id")) {
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        intent.putExtra("username", obj.getString("username"));
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse error:" + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", etUsername.getText().toString());
                        params.put("pw", etPassword.getText().toString());
                        return params;
                    }
                };

                queue.add(strRequest);
            }
        });

        Button mBtSignUp = (Button) findViewById(chiYo.MarketPlace.R.id.btSignUp);
        mBtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), signUp.class);
                intent.putExtra("username", etUsername.getText().toString());
                intent.putExtra("pw", etPassword.getText().toString());
                startActivity(intent);
            }
        });
    }
}
