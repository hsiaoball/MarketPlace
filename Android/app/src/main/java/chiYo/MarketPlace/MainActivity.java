package chiYo.MarketPlace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String backendURL = "http://kidvities-snowleo.rhcloud.com/";
    RequestQueue queue;
    GridView gridView;
    private CustomGridViewAdapter gridAdapter;
    private ArrayList<ItemPic> gridArray;
    private ArrayList<ItemPic> selectedItems;
    private ArrayList<Integer> selectedItemPrice;
    private ArrayList<String> selectedItemName;
    private TextView mTotalTv;
    private Button mBtCheckOut;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chiYo.MarketPlace.R.layout.activity_main);


        Log.i(TAG, "onCreate");
        TextView tvGreeting = (TextView) findViewById(chiYo.MarketPlace.R.id.tvGreeting);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tvGreeting.setText("Welcome "+username);
        mTotalTv = (TextView) findViewById(chiYo.MarketPlace.R.id.tvTotal);
        gridView = (GridView) findViewById(chiYo.MarketPlace.R.id.gridView);
        mBtCheckOut = (Button) findViewById(chiYo.MarketPlace.R.id.btCheckOut);
        gridArray = new ArrayList<>();
        selectedItems = new ArrayList<>();
        selectedItemName = new ArrayList<>();
        selectedItemPrice = new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        gridAdapter = new CustomGridViewAdapter(this, chiYo.MarketPlace.R.layout.row_grid, gridArray);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick position:" + position + " id:" + id);

                String clickedItemName = gridArray.get(position).getTitle();
                Integer selectedItemPrice = gridArray.get(position).getPrice();
                if (selectedItemName.contains(clickedItemName)) {
                    selectedItemName.remove(clickedItemName);
                    view.setBackgroundColor(Color.WHITE);
                    MainActivity.this.selectedItemPrice.remove(selectedItemPrice);
                } else {
                    selectedItemName.add(clickedItemName);
                    MainActivity.this.selectedItemPrice.add(selectedItemPrice);
                    view.setBackgroundColor(Color.GREEN);
                }
                int sum = 0;
                for (Integer i : MainActivity.this.selectedItemPrice) {
                    sum += i;
                }
                mTotalTv.setText("Total: $" + sum);
            }
        });
        mBtCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CheckOutActivity.class);
                intent.putStringArrayListExtra("selectedItemName", selectedItemName);
                intent.putIntegerArrayListExtra("selectedItemPrice", selectedItemPrice);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        getMerchandiseList();

    }

    public void getMerchandiseList(){
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        final int[] counter = {0};
        JsonArrayRequest arrayRequest = new JsonArrayRequest(backendURL+"polls/catalog/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseArray) {
                Log.i(TAG, "Responce:"+responseArray.toString());
                for(int i=0; i < responseArray.length(); i++){
                    JSONObject tempObj = null;
                    try {
                        tempObj = responseArray.getJSONObject(i);
                        final JSONObject fields = tempObj.getJSONObject("fields");
                        final int price = Integer.parseInt(fields.getString("price"));
                        final String name = fields.getString("pic");
                        ImageRequest request = new ImageRequest(backendURL+"static/pics/"+name+".jpeg",
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        Log.i(TAG, "onResponse");
                                        gridArray.add(new ItemPic(bitmap, name, price));
                                        gridAdapter.notifyDataSetChanged();
                                        counter[0]++;
                                        if(counter[0] == responseArray.length())
                                            dialog.dismiss();


                                    }
                                }, 0, 0, null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i(TAG, "onErrorResponse");
                                    }
                                });
                        queue.add(request);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e(TAG,"onErrorResponse error:"+error.toString());

            }
        });
        queue.add(arrayRequest);
    }





}


