package chiYo.MarketPlace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class signUp extends AppCompatActivity {
    private static final String TAG = signUp.class.getSimpleName();
    private String backendURL = "http://kidvities-snowleo.rhcloud.com/";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chiYo.MarketPlace.R.layout.activity_sign_up);
        Intent intentFromLogin = getIntent();
        queue = Volley.newRequestQueue(this);

        final EditText etUsername = (EditText) findViewById(chiYo.MarketPlace.R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(chiYo.MarketPlace.R.id.etPassWord);
        final EditText etPasswordRepeated = (EditText) findViewById(chiYo.MarketPlace.R.id.etPassWord2);
        etUsername.setText(intentFromLogin.getStringExtra("username"));
        etPassword.setText(intentFromLogin.getStringExtra("pw"));

        Button mBtSignUp = (Button) findViewById(chiYo.MarketPlace.R.id.btSignUp);
        mBtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etPassword.getText().toString().equals(etPasswordRepeated.getText().toString())){
                    Log.i(TAG, "pass word not equal");
                    TextView tvErrorMsg = (TextView) findViewById(chiYo.MarketPlace.R.id.tvErrorMsg);
                    tvErrorMsg.setText("Password is not matched another");
                    tvErrorMsg.setTextColor(Color.RED);
                    return;
                }
                final ProgressDialog dialog = ProgressDialog.show(signUp.this, "",
                        "Sign Up. Please wait...", true);
                StringRequest strRequest = new StringRequest(Request.Method.POST, backendURL + "login/create/",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "onResponse:" + response.toString());
                                try {
                                    JSONObject obj = new JSONObject(response);
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
    }
}
