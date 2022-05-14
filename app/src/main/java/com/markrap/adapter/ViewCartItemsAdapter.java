package com.markrap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.dashboard.outlet.ProductList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.markrap.R;


public class ViewCartItemsAdapter extends RecyclerView.Adapter<ViewCartItemsAdapter.MyViewHolder> {

    private AppCompatActivity ira1;
    private JSONArray moviesList;
    Context mContext;

    public Map<Integer, Integer> cartItems = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCateName, qty;



        public MyViewHolder(View view) {
            super(view);

            txtCateName = view.findViewById(R.id.txtCateName);
            qty = view.findViewById(R.id.qty);




        }
    }

    public ViewCartItemsAdapter(JSONArray moviesList, AppCompatActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public ViewCartItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewcart_list_row, parent, false);
        mContext = parent.getContext();
        return new ViewCartItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewCartItemsAdapter.MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);
            holder.txtCateName.setText(jsonObject.getString("title"));

            if(jsonObject.has("qty")) {
                holder.qty.setText("Quantity : " + jsonObject.getString("qty"));//when order placed
            }
            else
            {
                holder.qty.setText("Quantity : " + jsonObject.getString("quanity"));//when order placed
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }






}
