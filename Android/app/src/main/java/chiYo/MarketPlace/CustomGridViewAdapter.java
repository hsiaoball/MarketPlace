package chiYo.MarketPlace;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomGridViewAdapter extends ArrayAdapter<ItemPic> {
    Context context;
    int layoutResourceId;
    ArrayList<ItemPic> data = new ArrayList<>();

    public CustomGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<ItemPic> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(chiYo.MarketPlace.R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(chiYo.MarketPlace.R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }


        ItemPic item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imageItem.setImageBitmap(item.getImage());
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }
}

class ItemPic {
    String mTitle;
    int mPrice;
    Bitmap mImage;

    public ItemPic(Bitmap image, String title, int price){
        mImage = image;
        mTitle = title;
        mPrice = price;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getPrice() {
        return mPrice;
    }
}