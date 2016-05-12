package chiYo.MarketPlace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckOutActivity extends AppCompatActivity {

    private ArrayList<String> selectedItemNames;
    private ArrayList<Integer> selectedItemPrices;
    private ArrayList<String> mAlItems;
    private ListView mLvCheckOutItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chiYo.MarketPlace.R.layout.activity_check_out);

        final Intent intent = getIntent();
        mAlItems = new ArrayList<>();
        selectedItemNames = intent.getStringArrayListExtra("selectedItemName");
        selectedItemPrices = intent.getIntegerArrayListExtra("selectedItemPrice");
        String username = intent.getStringExtra("username");
        TextView tvCheckOut = (TextView) findViewById(chiYo.MarketPlace.R.id.tvCheckOut);
        tvCheckOut.setText("Hi "+username+", here are your purchase order");
        for(int i = 0; i< selectedItemNames.size(); i++){
            mAlItems.add(selectedItemNames.get(i) + " $" + selectedItemPrices.get(i));
        }
        mLvCheckOutItems = (ListView) findViewById(chiYo.MarketPlace.R.id.lvCheckOutItems);

        mLvCheckOutItems.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mAlItems));
        Button btPlaceOrder = (Button) findViewById(chiYo.MarketPlace.R.id.btPlaceOrder);
        btPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToPlaceOrder = new Intent(getBaseContext(), CompleteOrder.class);
                startActivity(intentToPlaceOrder);
            }
        });

    }

}
