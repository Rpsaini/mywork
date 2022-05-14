package com.markrap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;

import org.json.JSONArray;


public class ManageProductCartAdapter extends RecyclerView.Adapter<ManageProductCartAdapter.MyViewHolder> {

    private AppCompatActivity ira1;
    private JSONArray moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chSelected;
        public MyViewHolder(View view) {
            super(view);


        }
    }
    public ManageProductCartAdapter(JSONArray moviesList, AppCompatActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_product_view, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

//            JSONObject jsonObject = moviesList.getJSONObject(position);
//            holder.chSelected.setText(jsonObject.getString("name"));
//            holder.chSelected.setTag(jsonObject);


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.length()+15;
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
